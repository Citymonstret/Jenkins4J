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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * Information regarding a Jenkins build
 * <p>
 * This is a child to {@link JobInfo}, and has the direct child {@link ArtifactDescription}
 * {@inheritDoc}
 */
@SuppressWarnings("unused") public final class BuildInfo
    implements Node, NodeChild<JobInfo>, NodePath {

    private final Jenkins jenkins;
    private final boolean building;
    private final String result;
    private final String displayName;
    private final String fullDisplayName;
    private final int id;
    private final long duration;
    private final long timestamp;
    private final String url;
    private final Collection<ArtifactDescription> artifacts;

    private JobInfo parent;

    /**
     * Instantiates a new Build info.
     *
     * @param jenkins         the jenkins
     * @param building        the building
     * @param result          the result
     * @param displayName     the display name
     * @param fullDisplayName the full display name
     * @param id              the id
     * @param duration        the duration
     * @param timestamp       the timestamp
     * @param url             the url
     * @param artifacts       the artifacts
     */
    public BuildInfo(@NotNull final Jenkins jenkins, final boolean building,
        @Nullable final String result, @NotNull final String displayName,
        @NotNull final String fullDisplayName, final int id, final long duration,
        final long timestamp, @NotNull final String url,
        @NotNull final Collection<ArtifactDescription> artifacts) {
        this.jenkins = Preconditions.checkNotNull(jenkins, "Jenkins may not be null");
        this.building = building;
        this.result = result;
        this.displayName = Preconditions.checkNotNull(displayName, "Display name may not be null");
        this.fullDisplayName =
            Preconditions.checkNotNull(fullDisplayName, "Full display name may not be null");
        this.id = id;
        this.duration = duration;
        this.timestamp = timestamp;
        this.url = Preconditions.checkNotNull(url, "URL may not be null");
        this.artifacts = Preconditions.checkNotNull(artifacts, "Artifacts may not be null");
    }

    /**
     * Is building boolean.
     *
     * @return the boolean
     */
    public boolean isBuilding() {
        return building;
    }

    /**
     * Gets result.
     *
     * @return the result
     */
    public String getResult() {
        return result;
    }

    /**
     * Gets display name.
     *
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Gets full display name.
     *
     * @return the full display name
     */
    public String getFullDisplayName() {
        return fullDisplayName;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Gets duration.
     *
     * @return the duration
     */
    public long getDuration() {
        return duration;
    }

    /**
     * Gets timestamp.
     *
     * @return the timestamp
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Get the url to the build on Jenkins
     *
     * @return build url
     */
    @Override public String getUrl() {
        return url;
    }

    /**
     * Get all artifacts produced by this build
     *
     * @return artifact descriptions
     */
    @NotNull public Collection<ArtifactDescription> getArtifacts() {
        return Collections.unmodifiableCollection(this.artifacts);
    }

    @Override public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final BuildInfo buildInfo = (BuildInfo) o;
        return building == buildInfo.building && id == buildInfo.id
            && duration == buildInfo.duration && timestamp == buildInfo.timestamp && Objects
            .equals(result, buildInfo.result) && displayName.equals(buildInfo.displayName)
            && fullDisplayName.equals(buildInfo.fullDisplayName) && url.equals(buildInfo.url)
            && Objects.equals(artifacts, buildInfo.artifacts);
    }

    @Override public int hashCode() {
        return Objects
            .hash(building, result, displayName, fullDisplayName, id, duration, timestamp, url,
                artifacts);
    }

    @NotNull @Override public String toString() {
        return "BuildInfo{" + "building=" + building + ", result='" + result + '\''
            + ", displayName='" + displayName + '\'' + ", fullDisplayName='" + fullDisplayName
            + '\'' + ", id=" + id + ", duration=" + duration + ", timestamp=" + timestamp
            + ", url='" + url + '\'' + ", artifacts=" + artifacts + '}';
    }

    @NotNull @Override public CompletableFuture<JobInfo> getParent() {
        if (this.parent != null) {
            return CompletableFuture.completedFuture(this.parent);
        }
        // We need to find the job name
        String url = this.url;
        // The urls look something like this $baseurl/job/$jobname/$buildnum/"
        // we can get the base url, so let's do that
        final String baseUrl = this.jenkins.getJenkinsPathProvider().getBasePath();
        // let's remove that
        url = url.replace(baseUrl, "");
        // now we should have job/$jobname/$buildnum/
        final String[] pieces = url.split("/");
        final String jobName;
        if (pieces.length == 2) {
            // Oh-oh.
            if (pieces[0].equalsIgnoreCase("job")) {
                jobName = pieces[1];
            } else {
                jobName = pieces[0];
            }
        } else if (pieces.length == 3) {
            if (pieces[0].equalsIgnoreCase("job")) {
                jobName = pieces[1];
            } else if (pieces[1].equalsIgnoreCase("job")) {
                jobName = pieces[2];
            } else {
                throw new IllegalStateException(
                    String.format("Could not extract job name from URL: %s", url));
            }
        } else {
            throw new IllegalStateException(
                String.format("Could not extract job name from URL: %s", url));
        }
        return this.jenkins.getJobInfo(jobName);
    }

    public void setParent(@NotNull final JobInfo parent) {
        if (this.parent != null) {
            throw new IllegalStateException("Cannot re-set node parent");
        }
        this.parent = parent;
    }

    @NotNull @Override public Jenkins getJenkins() {
        return this.jenkins;
    }

}
