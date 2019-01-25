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
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * The type Job description.
 * {@inheritDoc}
 */
@SuppressWarnings("unused") public final class JobDescription
    implements NodeChild<MasterNode>, NodePath {

    private final String jenkinsClass;
    private final String name;
    private final String url;
    private final String color;

    private MasterNode parent;

    /**
     * Instantiates a new Job description.
     *
     * @param jenkinsClass the jenkins class
     * @param name         the name
     * @param url          the url
     * @param color        the color
     */
    public JobDescription(@NotNull final String jenkinsClass, @NotNull final String name,
        @NotNull final String url, @NotNull final String color) {
        Preconditions.checkNotNull(jenkinsClass, "Jenkins class may not be null");
        Preconditions.checkNotNull(name, "Name may not be null");
        Preconditions.checkNotNull(url, "Url may not be null");
        Preconditions.checkNotNull(color, "Color may not be null");
        this.jenkinsClass = jenkinsClass;
        this.name = name;
        this.url = url;
        this.color = color;
    }

    /**
     * Gets jenkins class.
     *
     * @return the jenkins class
     */
    @Contract(pure = true) public String getJenkinsClass() {
        return this.jenkinsClass;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    @Contract(pure = true) public String getName() {
        return this.name;
    }

    /**
     * Get the jenkins URL of the job that this description is representing
     *
     * @return job url
     */
    @Override @Contract(pure = true) public String getUrl() {
        return this.url;
    }

    /**
     * Gets color.
     *
     * @return the color
     */
    @Contract(pure = true) public String getColor() {
        return this.color;
    }

    @Contract(value = "null -> false", pure = true) @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final JobDescription that = (JobDescription) o;
        return jenkinsClass.equals(that.jenkinsClass) && name.equals(that.name) && url
            .equals(that.url) && color.equals(that.color);
    }

    @Override public int hashCode() {
        return Objects.hash(jenkinsClass, name, url, color);
    }

    @NotNull @Contract(pure = true) @Override public String toString() {
        return "JobDescription{" + "jenkinsClass='" + jenkinsClass + '\'' + ", name='" + name + '\''
            + ", url='" + url + '\'' + ", color='" + color + '\'' + '}';
    }

    @NotNull @Contract(pure = true) @Override public CompletableFuture<MasterNode> getParent() {
        return CompletableFuture.completedFuture(this.parent);
    }

    @Override public void setParent(@NotNull final MasterNode parent) {
        if (this.parent != null) {
            throw new IllegalStateException("Cannot re-set node parent");
        }
        this.parent = parent;
    }

    /**
     * Get the {@link JobInfo} described by this {@link JobDescription}
     *
     * @return the job info
     */
    public CompletableFuture<JobInfo> getJobInfo() {
        return this.parent.getJobInfo(this.name);
    }

}
