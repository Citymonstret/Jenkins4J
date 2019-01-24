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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class TraverseBuilds {

    private static final int MAX_JOBS = 20;

    public static void main(String[] args) {
        final Jenkins jenkins = Jenkins.newBuilder().withPath("https://ci.athion.net/").build();
        try {
            System.out.println("- Getting master node");
            final MasterNode masterNode = jenkins.getMasterNode().get();
            List<JobDescription> masterNodeJobDescriptions = new ArrayList<>(masterNode.getJobDescriptions());
            // limit list size
            if (masterNodeJobDescriptions.size() > MAX_JOBS) {
                final int max = masterNodeJobDescriptions.size();
                final int min = max - MAX_JOBS;
                masterNodeJobDescriptions = masterNodeJobDescriptions.subList(min, max);
            }
            System.out.printf("-- Found %d job descriptions\n", masterNodeJobDescriptions.size());
            System.out.println("-- Looping through job descriptions");
            masterNodeJobDescriptions.forEach(jobDescription -> {
                System.out.printf("--- Found job description with name '%s'\n", jobDescription.getName());
                System.out.println("---- Getting job information");
                try {
                    final JobInfo jobInfo = jobDescription.getJobInfo().get();
                    final Collection<BuildDescription> jobNodeBuildDescriptions = jobInfo.getBuilds();
                    System.out.printf("---- Found %d build descriptions\n", jobNodeBuildDescriptions.size());
                    System.out.println("---- Looping through build descriptions");
                    jobNodeBuildDescriptions.forEach(buildDescription -> {
                        System.out.printf("----- Found build with build number #%d\n", buildDescription.getNumber());
                        System.out.println("----- Getting build info");
                        try {
                            final BuildInfo buildInfo = buildDescription.getBuildInfo().get();
                            final Collection<ArtifactDescription> buildNodeArtifactDescriptions = buildInfo.getArtifacts();
                            System.out.printf("------ Found %d artifacts\n", buildNodeArtifactDescriptions.size());
                            System.out.println("------ Listing artifacts: ");
                            buildNodeArtifactDescriptions.forEach(artifactDescription -> {
                                System.out.printf("------- %s\n", artifactDescription.getFileName());
                            });
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }
                    });
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            });
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

}
