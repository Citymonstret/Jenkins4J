//
// MIT License
//
// Copyright (c) 2019 Alexander Söderberg
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.
//

package org.incendo.jenkins;

import org.incendo.jenkins.exception.JenkinsJobNotFoundException;
import org.incendo.jenkins.exception.JenkinsNodeReadException;
import org.incendo.jenkins.exception.JenkinsNotAuthenticatedException;
import org.incendo.jenkins.objects.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

class JenkinsTest {

    private static final String BASE_PATH = "http://localhost:1080/";
    private static final int EXPECTED_JOBS = 15;
    private static final String REAL_JOB = "PlotSquared";
    private static final int REAL_BUILD = 1;
    private static final String FAKE_JOB = "FakeJob";
    private static final String ILLEGAL_JOB = "IllegalJob";
    private static final int ILLEGAL_BUILD = 1;

    private static ClientAndServer mockServer;

    private static String readResourceContent(@Nonnull final String resourceName) throws Exception {
        final StringBuilder builder = new StringBuilder();
        final InputStream inputStreamReader = JenkinsTest.class.getClassLoader().getResourceAsStream(resourceName);
        assertNotNull(inputStreamReader);
        try (final BufferedReader reader = new BufferedReader(
            new InputStreamReader(inputStreamReader))) {
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
        }
        return builder.toString();
    }

    @BeforeAll static void setupServer() throws Throwable {
        mockServer = ClientAndServer.startClientAndServer(1080);
        // Read the mock data
        final String mainNodeResponse = readResourceContent("main_node_successful.json");
        final String jobResponse = readResourceContent("job_plotsquared_successful.json");
        final String buildResponse = readResourceContent("job_plotsquared_build_successful.json");
        // Setup mock main node using data from an actual Jenkins instance
        mockServer.when(HttpRequest.request().withMethod("GET").withPath("/api/json"))
            .respond(HttpResponse.response().withStatusCode(200)
                .withHeader("X-Jenkins", "2.160").withBody(mainNodeResponse));
        mockServer.when(HttpRequest.request().withMethod("GET").withPath("/job/PlotSquared/api/json"))
            .respond(HttpResponse.response().withStatusCode(200)
                .withHeader("X-Jenkins", "2.160").withBody(jobResponse));
        mockServer.when(HttpRequest.request().withMethod("GET").withPath("/job/PlotSquared/1/api/json"))
            .respond(HttpResponse.response().withStatusCode(200)
                .withHeader("X-Jenkins", "2.160").withBody(buildResponse));
        // Setup 404 and 403 responses
        mockServer.when(HttpRequest.request().withMethod("GET").withPath("/job/FakeJob/api/json"))
            .respond(HttpResponse.response().withStatusCode(404));
        mockServer.when(HttpRequest.request().withMethod("GET").withPath("/job/IllegalJob/1/api/json"))
            .respond(HttpResponse.response().withStatusCode(403));
    }

    @AfterAll static void stopServer() {
        mockServer.stop();
    }

    private JenkinsBuilder newBuilder() {
        return Jenkins.newBuilder();
    }

    @Test void withPath() {
        final JenkinsBuilder jenkinsBuilder = newBuilder();
        assertNotNull(jenkinsBuilder);
        assertNotNull(mockServer);
        // covers both IllegalArgumentException and NullPointerException
        //noinspection ConstantConditions
        assertThrows(RuntimeException.class, () -> jenkinsBuilder.withPath(null));
        assertThrows(IllegalStateException.class, () -> jenkinsBuilder.withPath(""));
        assertDoesNotThrow(() ->  jenkinsBuilder.withPath(BASE_PATH));
        assertEquals(jenkinsBuilder, jenkinsBuilder.withPath(BASE_PATH));
    }

    @SuppressWarnings("ConstantConditions") @Test void withBasicAuthentication() {
        final JenkinsBuilder jenkinsBuilder = newBuilder();
        assertNotNull(jenkinsBuilder);
        // covers both IllegalArgumentException and NullPointerException
        assertThrows(RuntimeException.class, () -> jenkinsBuilder.withBasicAuthentication(null, null));
        assertThrows(RuntimeException.class, () -> jenkinsBuilder.withBasicAuthentication(null, ""));
        assertThrows(RuntimeException.class, () -> jenkinsBuilder.withBasicAuthentication("", null));
        assertEquals(jenkinsBuilder, jenkinsBuilder.withBasicAuthentication("", ""));
    }

    @Test void build() {
        final String testURL = "https://random.jenkins.path";
        final JenkinsBuilder jenkinsBuilder = newBuilder();
        assertNotNull(jenkinsBuilder);
        assertThrows(RuntimeException.class, jenkinsBuilder::build);
        jenkinsBuilder.withPath(testURL);
        assertDoesNotThrow(jenkinsBuilder::build);
        Jenkins jenkins = jenkinsBuilder.build();
        assertNotNull(jenkins);
        final JenkinsPathProvider jenkinsPathProvider = jenkins.getJenkinsPathProvider();
        assertNotNull(jenkinsPathProvider);
        assertNotEquals(testURL, jenkinsPathProvider.getBasePath());
        assertEquals(String.format("%s/", testURL), jenkinsPathProvider.getBasePath());
        jenkinsBuilder.withBasicAuthentication("username", "password");
        jenkins = jenkinsBuilder.build();
        assertTrue(jenkins.getJenkinsAuthentication() instanceof JenkinsBasicAuthentication);
    }

    @Test void fetchMainNode() throws ExecutionException, InterruptedException {
        final JenkinsBuilder jenkinsBuilder = newBuilder();
        assertNotNull(jenkinsBuilder);
        jenkinsBuilder.withPath(BASE_PATH);
        final Jenkins jenkins = jenkinsBuilder.build();
        assertNotNull(jenkins);
        final CompletableFuture<MasterNode> masterNodeCompletableFuture = jenkins.getMasterNode();
        final MasterNode node = masterNodeCompletableFuture.get();
        assertNotNull(node);
        assertEquals(BASE_PATH, node.getUrl());
        assertEquals(jenkins, node.getJenkins());
        final Collection<JobDescription> jobDescriptions = node.getJobDescriptions();
        assertNotNull(jobDescriptions);
        assertEquals(EXPECTED_JOBS, jobDescriptions.size());
        for (final JobDescription jobDescription : jobDescriptions) {
            assertNotNull(jobDescription);
            assertNotNull(jobDescription.getName());
            assertFalse(jobDescription.getName().isEmpty());
            assertNotNull(jobDescription.getUrl());
            assertFalse(jobDescription.getUrl().isEmpty());
            assertEquals(jobDescription.getParent().get(), node);
        }
    }

    @Test void fetchJobNode() throws ExecutionException, InterruptedException {
        final JenkinsBuilder jenkinsBuilder = newBuilder();
        assertNotNull(jenkinsBuilder);
        jenkinsBuilder.withPath(BASE_PATH);
        final Jenkins jenkins = jenkinsBuilder.build();
        assertNotNull(jenkins);
        final CompletableFuture<JobInfo> jobInfoCompletableFuture = jenkins.getJobInfo(REAL_JOB);
        final JobInfo jobInfo = jobInfoCompletableFuture.get();
        assertNotNull(jobInfo);
        assertEquals("PlotSquared", jobInfo.getDisplayName());
        assertEquals("https://ci.athion.net/job/PlotSquared/", jobInfo.getUrl());
        final Collection<BuildDescription> buildDescriptions = jobInfo.getBuilds();
        assertNotNull(buildDescriptions);
        assertEquals(100, buildDescriptions.size());
        for (final BuildDescription buildDescription : buildDescriptions) {
            assertNotNull(buildDescription);
            assertTrue(buildDescription.getNumber() > 0); // ensure positive
            assertNotNull(buildDescription.getUrl());
            assertEquals(buildDescription.getParent().get(), jobInfo);
        }
        assertNotNull(jobInfo.getLastSuccessfulBuild());
        assertEquals(686, jobInfo.getLastSuccessfulBuild().getNumber());
        final BuildDescription lastBuild = jobInfo.getLastBuild();
        assertNotNull(lastBuild);
        assertEquals(lastBuild.getNumber() + 1, jobInfo.getNextBuildNumber());
    }

    @Test void testJobInfo() throws ExecutionException, InterruptedException {
        final JenkinsBuilder jenkinsBuilder = newBuilder();
        assertNotNull(jenkinsBuilder);
        jenkinsBuilder.withPath(BASE_PATH);
        final Jenkins jenkins = jenkinsBuilder.build();
        assertNotNull(jenkins);
        final CompletableFuture<BuildInfo> buildInfoCompletableFuture =
            jenkins.getBuildInfo(REAL_JOB, REAL_BUILD);
        final BuildInfo buildInfo = buildInfoCompletableFuture.get();
        assertNotNull(buildInfo);
        final Collection<ArtifactDescription> artifactDescriptions = buildInfo.getArtifacts();
        assertNotNull(artifactDescriptions);
        for (final ArtifactDescription artifactDescription : artifactDescriptions) {
            assertNotNull(artifactDescription);
            assertNotNull(artifactDescription.getDisplayPath());
            assertNotNull(artifactDescription.getFileName());
            assertNotNull(artifactDescription.getUrl());
            assertNotNull(artifactDescription.getRelativePath());
            assertEquals(artifactDescription.getParent().get(), buildInfo);
            assertEquals(String.format("%s/job/%s/%d/artifact/%s", "https://ci.athion.net",
                REAL_JOB, REAL_BUILD, artifactDescription.getRelativePath()), artifactDescription.getUrl());
        }
    }

    @Test void testFakeJob() {
        final JenkinsBuilder jenkinsBuilder = newBuilder();
        assertNotNull(jenkinsBuilder);
        jenkinsBuilder.withPath(BASE_PATH);
        final Jenkins jenkins = jenkinsBuilder.build();
        assertNotNull(jenkins);
        final CompletableFuture<JobInfo> jobInfoCompletableFuture = jenkins.getJobInfo(FAKE_JOB);
        final ExecutionException executionException = assertThrows(ExecutionException.class, jobInfoCompletableFuture::get);
        final Throwable cause = executionException.getCause();
        assertTrue(cause instanceof JenkinsNodeReadException);
        assertTrue(cause.getCause() instanceof JenkinsJobNotFoundException);
    }

    @Test void testIllegalBuild() {
        final JenkinsBuilder jenkinsBuilder = newBuilder();
        assertNotNull(jenkinsBuilder);
        jenkinsBuilder.withPath(BASE_PATH);
        final Jenkins jenkins = jenkinsBuilder.build();
        assertNotNull(jenkins);
        final CompletableFuture<BuildInfo> buildInfoCompletableFuture = jenkins.getBuildInfo(ILLEGAL_JOB, ILLEGAL_BUILD);
        final ExecutionException executionException = assertThrows(ExecutionException.class, buildInfoCompletableFuture::get);
        final Throwable cause = executionException.getCause();
        assertTrue(cause instanceof JenkinsNodeReadException);
        assertTrue(cause.getCause() instanceof JenkinsNotAuthenticatedException);
    }

}
