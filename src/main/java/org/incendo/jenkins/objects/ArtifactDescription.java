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
 * Artifact descriptions are what is listed on Jenkins as artifacts. This class can be used
 * to get a downloadable artifact link ({@link #getUrl()}
 * <p>
 * This is a child to {@link BuildInfo}
 */
@SuppressWarnings("unused") public final class ArtifactDescription
    implements NodeChild<BuildInfo>, NodePath {

    private final String displayPath;
    private final String fileName;
    private final String relativePath;

    private BuildInfo parent;

    /**
     * Instantiates a new Artifact description.
     *
     * @param displayPath  the display path
     * @param fileName     the file name
     * @param relativePath the relative path
     */
    public ArtifactDescription(@NotNull final String displayPath, @NotNull final String fileName,
        @NotNull final String relativePath) {
        this.displayPath = Preconditions.checkNotNull(displayPath, "Display path may not be null");
        this.fileName = Preconditions.checkNotNull(fileName, "File name may not be null");
        this.relativePath =
            Preconditions.checkNotNull(relativePath, "Relative path may not be null");
    }

    /**
     * Gets display path.
     *
     * @return the display path
     */
    @Contract(pure = true) public String getDisplayPath() {
        return displayPath;
    }

    /**
     * Gets file name.
     *
     * @return the file name
     */
    @Contract(pure = true) public String getFileName() {
        return fileName;
    }

    /**
     * Gets relative path.
     *
     * @return the relative path
     */
    @Contract(pure = true) public String getRelativePath() {
        return relativePath;
    }

    @Contract(value = "null -> false", pure = true) @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ArtifactDescription that = (ArtifactDescription) o;
        return displayPath.equals(that.displayPath) && fileName.equals(that.fileName)
            && relativePath.equals(that.relativePath);
    }

    @Override public int hashCode() {
        return Objects.hash(displayPath, fileName, relativePath);
    }

    @NotNull @Contract(pure = true) @Override public String toString() {
        return "ArtifactInfo{" + "displayPath='" + displayPath + '\'' + ", fileName='" + fileName
            + '\'' + ", relativePath='" + relativePath + '\'' + '}';
    }

    @NotNull @Contract(pure = true) @Override public CompletableFuture<BuildInfo> getParent() {
        return CompletableFuture.completedFuture(this.parent);
    }

    @Override public void setParent(@NotNull final BuildInfo parent) {
        if (this.parent != null) {
            throw new IllegalStateException("Cannot re-set node parent");
        }
        this.parent = parent;
    }

    /**
     * Get the url pointing to this artifact
     *
     * @return artifact url
     */
    @Override public String getUrl() {
        final String path;
        if (this.relativePath.startsWith("/")) {
            path = this.relativePath.substring(1);
        } else {
            path = relativePath;
        }
        final String jobUrl;
        if (this.parent.getUrl().endsWith("/")) {
            jobUrl = this.parent.getUrl().substring(0, this.parent.getUrl().length() - 1);
        } else {
            jobUrl = this.parent.getUrl();
        }
        return String.format("%s/artifact/%s", jobUrl, path);
    }
}
