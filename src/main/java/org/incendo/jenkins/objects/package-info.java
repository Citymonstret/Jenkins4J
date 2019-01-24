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

/**
 * Java representations of Jenkins elements
 * <p>
 * There are two types of objects that you will encounter when working with Jenkins4J:
 * <ul>
 * <li>description classes</li>
 * <li>information classes</li>
 * </ul>
 * <p>
 * A description is a simple member of an info class, and it will often just contain a name or a number.
 * You can use a description to get a ({@link java.util.concurrent.CompletableFuture} of) an
 * information class. Information classes contain references to child nodes, and a bunch of meta
 * data. This is to mirror how the Jenkins API works.
 * <p>
 * You can use description classes to retrieve their corresponding information classes, by using
 * {@link org.incendo.jenkins.objects.NodeChild#getParent()}.
 * All information classes can also be retrieved using the Jenkins instance.
 */
package org.incendo.jenkins.objects;
