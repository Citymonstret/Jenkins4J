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

package org.incendo.jenkins.objects;

import com.google.common.base.Preconditions;
import org.incendo.jenkins.Jenkins;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

/**
 * The type Job info.
 * {@inheritDoc}
 */
@SuppressWarnings({"unused", "WeakerAccess"}) public final class JobInfo
    implements Node, NodeChild<MasterNode>, NodePath {

    private final Jenkins jenkins;
    private final String name;
    private final String fullName;
    private final String displayName;
    private final String fullDisplayName;
    private final String description;
    private final String url;
    private final Collection<BuildDescription> builds;
    private final BuildDescription lastBuild;
    private final BuildDescription lastCompletedBuild;
    private final BuildDescription lastFailedBuild;
    private final BuildDescription lastSuccessfulBuild;
    private final int nextBuildNumber;

    /**
     * Instantiates a new Job info.
     *
     * @param jenkins             the jenkins
     * @param name                the name
     * @param fullName            the full name
     * @param displayName         the display name
     * @param fullDisplayName     the full display name
     * @param description         the description
     * @param url                 the url
     * @param builds              the builds
     * @param lastBuild           the last build
     * @param lastCompletedBuild  the last completed build
     * @param lastFailedBuild     the last failed build
     * @param lastSuccessfulBuild the last successful build
     * @param nextBuildNumber     the next build number
     */
    public JobInfo(@NotNull final Jenkins jenkins, @NotNull final String name,
        @NotNull final String fullName, @NotNull final String displayName,
        @NotNull final String fullDisplayName, @NotNull final String description,
        @NotNull final String url, @NotNull final Collection<BuildDescription> builds,
        @Nullable final BuildDescription lastBuild,
        @Nullable final BuildDescription lastCompletedBuild,
        @Nullable final BuildDescription lastFailedBuild,
        @Nullable final BuildDescription lastSuccessfulBuild, final int nextBuildNumber) {
        this.jenkins = Preconditions.checkNotNull(jenkins, "Jenkins may not be null");
        this.name = Preconditions.checkNotNull(name, "Name may not be null");
        this.fullName = Preconditions.checkNotNull(fullName, "Full name may not be null");
        this.displayName = Preconditions.checkNotNull(displayName, "Display name may not be null");
        this.fullDisplayName =
            Preconditions.checkNotNull(fullDisplayName, "Full display name may not be null");
        this.description = Preconditions.checkNotNull(description, "Description may not be null");
        this.url = Preconditions.checkNotNull(url, "Url may not be null");
        this.builds = Preconditions.checkNotNull(builds);
        this.lastBuild = lastBuild;
        this.lastCompletedBuild = lastCompletedBuild;
        this.lastFailedBuild = lastFailedBuild;
        this.lastSuccessfulBuild = lastSuccessfulBuild;
        this.nextBuildNumber = nextBuildNumber;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    @Contract(pure = true) public String getName() {
        return name;
    }

    /**
     * Gets full name.
     *
     * @return the full name
     */
    @Contract(pure = true) public String getFullName() {
        return fullName;
    }

    /**
     * Gets display name.
     *
     * @return the display name
     */
    @Contract(pure = true) public String getDisplayName() {
        return displayName;
    }

    /**
     * Gets full display name.
     *
     * @return the full display name
     */
    @Contract(pure = true) public String getFullDisplayName() {
        return fullDisplayName;
    }

    /**
     * Gets description.
     *
     * @return the description
     */
    @Contract(pure = true) public String getDescription() {
        return description;
    }

    /**
     * Get the jenkins URL of the job that this description is representing
     *
     * @return job url
     */
    @Override @Contract(pure = true) public String getUrl() {
        return url;
    }

    /**
     * Gets builds.
     *
     * @return the builds
     */
    @Contract(pure = true) public Collection<BuildDescription> getBuilds() {
        return builds;
    }

    /**
     * Gets last build.
     *
     * @return the last build
     */
    @Contract(pure = true) public BuildDescription getLastBuild() {
        return lastBuild;
    }

    /**
     * Gets last completed build.
     *
     * @return the last completed build
     */
    @Contract(pure = true) public BuildDescription getLastCompletedBuild() {
        return lastCompletedBuild;
    }

    /**
     * Gets last failed build.
     *
     * @return the last failed build
     */
    @Contract(pure = true) public BuildDescription getLastFailedBuild() {
        return lastFailedBuild;
    }

    /**
     * Gets last successful build.
     *
     * @return the last successful build
     */
    @Contract(pure = true) public BuildDescription getLastSuccessfulBuild() {
        return lastSuccessfulBuild;
    }

    /**
     * Gets next build number.
     *
     * @return the next build number
     */
    @Contract(pure = true) public int getNextBuildNumber() {
        return nextBuildNumber;
    }

    @NotNull @Contract(pure = true) @Override public String toString() {
        return "JobInfo{" + "name='" + name + '\'' + ", fullName='" + fullName + '\''
            + ", displayName='" + displayName + '\'' + ", fullDisplayName='" + fullDisplayName
            + '\'' + ", description='" + description + '\'' + ", url='" + url + '\'' + ", builds="
            + builds + ", lastBuild=" + lastBuild + ", lastCompletedBuild=" + lastCompletedBuild
            + ", lastFailedBuild=" + lastFailedBuild + ", lastSuccessfulBuild="
            + lastSuccessfulBuild + ", nextBuildNumber=" + nextBuildNumber + '}';
    }

    @Contract(pure = true) @Override public Jenkins getJenkins() {
        return this.jenkins;
    }

    @Override public CompletableFuture<MasterNode> getParent() {
        return this.jenkins.getMasterNode();
    }

    @Override public void setParent(@NotNull MasterNode parent) {
    }

    /**
     * Gets build info.
     *
     * @param number the number
     * @return the build info
     */
    public CompletableFuture<BuildInfo> getBuildInfo(final int number) {
        return this.jenkins.getBuildInfo(this.name, number).thenApply(buildInfo -> {
            buildInfo.setParent(JobInfo.this);
            return buildInfo;
        });
    }

}
