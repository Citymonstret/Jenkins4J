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
 * Build description, which a reference to a Jenkins job build
 * <p>
 * This is a direct child to {@link JobInfo}
 * {@inheritDoc}
 */
@SuppressWarnings("unused") public final class BuildDescription
    implements NodeChild<JobInfo>, NodePath {

    private final String jenkinsClass;
    private final int number;
    private final String url;

    private JobInfo parent;

    /**
     * Instantiates a new Build description.
     *
     * @param jenkinsClass the jenkins class
     * @param number       the number
     * @param url          the url
     */
    public BuildDescription(@NotNull final String jenkinsClass, final int number,
        @NotNull final String url) {
        Preconditions.checkNotNull(jenkinsClass, "Jenkins class may not be null");
        Preconditions.checkNotNull(url, "Url may not be null");
        this.jenkinsClass = jenkinsClass;
        this.number = number;
        this.url = url;
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
     * Get the build number
     *
     * @return Build number
     */
    @Contract(pure = true) public int getNumber() {
        return this.number;
    }

    /**
     * Get an URL linking to the build that this description is representing
     *
     * @return build url
     */
    @Override @Contract(pure = true) public String getUrl() {
        return this.url;
    }

    @Contract(value = "null -> false", pure = true) @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final BuildDescription that = (BuildDescription) o;
        return number == that.number && jenkinsClass.equals(that.jenkinsClass) && url
            .equals(that.url);
    }

    @Override public int hashCode() {
        return Objects.hash(jenkinsClass, number, url);
    }

    @NotNull @Contract(pure = true) @Override public String toString() {
        return "BuildDescription{" + "jenkinsClass='" + jenkinsClass + '\'' + ", number=" + number
            + ", url='" + url + '\'' + '}';
    }

    @NotNull @Contract(pure = true) @Override public CompletableFuture<JobInfo> getParent() {
        return CompletableFuture.completedFuture(this.parent);
    }

    @Override public void setParent(@NotNull final JobInfo parent) {
        if (this.parent != null) {
            throw new IllegalStateException("Cannot re-set node parent");
        }
        this.parent = parent;
    }

    /**
     * Get the {@link BuildInfo} represented by this description
     *
     * @return build info
     */
    public CompletableFuture<BuildInfo> getBuildInfo() {
        return this.parent.getBuildInfo(this.number);
    }

}
