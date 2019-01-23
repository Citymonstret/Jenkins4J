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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;

public final class JobInfo {

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

    public JobInfo(@Nonnull final String name, @Nonnull final String fullName, @Nonnull final String displayName,
        @Nonnull final String fullDisplayName, @Nonnull final String description, @Nonnull final String url,
        @Nonnull final Collection<BuildDescription> builds, @Nullable final BuildDescription lastBuild,
        @Nullable final BuildDescription lastCompletedBuild, @Nullable final BuildDescription lastFailedBuild,
        @Nullable final BuildDescription lastSuccessfulBuild, final int nextBuildNumber) {
        this.name = Preconditions.checkNotNull(name, "Name must not be null");
        this.fullName = Preconditions.checkNotNull(fullName, "Full name must not be null");
        this.displayName = Preconditions.checkNotNull(displayName, "Display name must not be null");
        this.fullDisplayName = Preconditions.checkNotNull(fullDisplayName, "Full display name must not be null");
        this.description = Preconditions.checkNotNull(description, "Description must not be null");
        this.url = Preconditions.checkNotNull(url, "Url must not be null");
        this.builds = Preconditions.checkNotNull(builds);
        this.lastBuild = lastBuild;
        this.lastCompletedBuild = lastCompletedBuild;
        this.lastFailedBuild = lastFailedBuild;
        this.lastSuccessfulBuild = lastSuccessfulBuild;
        this.nextBuildNumber = nextBuildNumber;
    }

    public String getName() {
        return name;
    }

    public String getFullName() {
        return fullName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getFullDisplayName() {
        return fullDisplayName;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public Collection<BuildDescription> getBuilds() {
        return builds;
    }

    public BuildDescription getLastBuild() {
        return lastBuild;
    }

    public BuildDescription getLastCompletedBuild() {
        return lastCompletedBuild;
    }

    public BuildDescription getLastFailedBuild() {
        return lastFailedBuild;
    }

    public BuildDescription getLastSuccessfulBuild() {
        return lastSuccessfulBuild;
    }

    public int getNextBuildNumber() {
        return nextBuildNumber;
    }

    @Override public String toString() {
        return "JobInfo{" + "name='" + name + '\'' + ", fullName='" + fullName + '\''
            + ", displayName='" + displayName + '\'' + ", fullDisplayName='" + fullDisplayName
            + '\'' + ", description='" + description + '\'' + ", url='" + url + '\'' + ", builds="
            + builds + ", lastBuild=" + lastBuild + ", lastCompletedBuild=" + lastCompletedBuild
            + ", lastFailedBuild=" + lastFailedBuild + ", lastSuccessfulBuild="
            + lastSuccessfulBuild + ", nextBuildNumber=" + nextBuildNumber + '}';
    }
}
