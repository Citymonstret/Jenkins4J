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

package examples;

import org.incendo.jenkins.Jenkins;
import org.incendo.jenkins.objects.ArtifactDescription;
import org.incendo.jenkins.objects.BuildDescription;
import org.incendo.jenkins.objects.BuildInfo;
import org.incendo.jenkins.objects.JobInfo;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FindLatest {

    private static final String JOB_NAME = "PlotSquared-Breaking";
    private static final Pattern ARTIFACT_PATTERN = Pattern.compile("^PlotSquared-Bukkit-([0-9.]+).jar$");

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        final Jenkins jenkins = Jenkins.newBuilder().withPath("https://ci.athion.net/").build();
        final CompletableFuture<JobInfo> jobInfoFuture = jenkins.getJobInfo(JOB_NAME);
        final JobInfo jobInfo = jobInfoFuture.get(); // Block until the item is ready
        final BuildDescription lastSuccess = jobInfo.getLastSuccessfulBuild();
        if (lastSuccess != null) {
            // There's a successful build. We want to get the artifacts, so we
            // will need to read the build information
            final CompletableFuture<BuildInfo> buildInfoFuture = lastSuccess.getBuildInfo();
            // an alternative would be to use the jenkins instance directly
            // final CompletableFuture<BuildInfo> buildInfoFuture = jenkins.getBuildInfo(JOB_NAME, lastSuccess.getNumber());
            final BuildInfo buildInfo = buildInfoFuture.get(); // Block until the item is ready
            final Collection<ArtifactDescription> artifacts = buildInfo.getArtifacts();
            // We are trying to find the Bukkit jar
            final Optional<ArtifactDescription> bukkitJar = artifacts.stream().filter(artifactDescription -> {
                final String name = artifactDescription.getFileName();
                final Matcher matcher = ARTIFACT_PATTERN.matcher(name);
                return matcher.matches();
            }).findAny();
            if (bukkitJar.isPresent()) {
                final ArtifactDescription artifact = bukkitJar.get();
                System.out.printf("Yay! Found the Bukkit jar %s in %s\n", artifact.getFileName(), JOB_NAME);
                System.out.printf("The url is '%s'\n", artifact.getUrl());
            } else {
                System.out.printf("Could not find a Bukkit jar in %s\n", JOB_NAME);
            }
        } else {
            System.out.printf("%s has no successful builds =(\n", JOB_NAME);
        }
    }

}
