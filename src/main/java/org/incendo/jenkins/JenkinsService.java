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

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Retrofit service, used to read Jenkins API response bodies
 */
interface JenkinsService {

    /**
     * Gets master node.
     *
     * @param type the type
     * @return the master node
     */
    @GET("api/{type}") Call<ResponseBody> getMasterNode(@Path("type") String type);

    /**
     * Gets job info.
     *
     * @param job  the job
     * @param type the type
     * @return the job info
     */
    @GET("job/{job}/api/{type}") Call<ResponseBody> getJobInfo(@Path("job") String job,
        @Path("type") String type);

    /**
     * Gets build info.
     *
     * @param job   the job
     * @param build the build
     * @param type  the type
     * @return the build info
     */
    @GET("job/{job}/{build}/api/{type}") Call<ResponseBody> getBuildInfo(@Path("job") String job,
        @Path("build") int build, @Path("type") String type);

}
