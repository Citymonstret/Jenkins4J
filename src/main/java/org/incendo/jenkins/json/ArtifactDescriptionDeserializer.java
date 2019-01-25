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

package org.incendo.jenkins.json;

import com.google.gson.*;
import org.incendo.jenkins.objects.ArtifactDescription;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

/**
 * Json deserializer for {@link ArtifactDescription}
 * {@inheritDoc}
 */
final class ArtifactDescriptionDeserializer implements JsonDeserializer<ArtifactDescription> {

    @NotNull @Contract(pure = true) @Override
    public ArtifactDescription deserialize(final JsonElement json, final Type typeOfT,
        final JsonDeserializationContext context) throws JsonParseException {
        final JsonObject jsonObject = json.getAsJsonObject();
        final String displayPath;
        if (jsonObject.has("displayPath")) {
            final JsonElement displayPathElement = jsonObject.get("displayPath");
            if (displayPathElement.isJsonNull()) {
                displayPath = "null";
            } else {
                displayPath = jsonObject.get("displayPath").getAsString();
            }
        } else {
            displayPath = "";
        }
        final String fileName;
        if (jsonObject.has("fileName")) {
            fileName = jsonObject.get("fileName").getAsString();
        } else {
            fileName = "";
        }
        final String relativePath;
        if (jsonObject.has("relativePath")) {
            relativePath = jsonObject.get("relativePath").getAsString();
        } else {
            relativePath = "";
        }
        return new ArtifactDescription(displayPath, fileName, relativePath);
    }

}
