/*
 * Imoji Android SDK
 * Created by nkhoshini
 *
 * Copyright (C) 2016 Imoji
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KID, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 *
 */

package io.imoji.sdk.objects.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.imoji.sdk.objects.Category;
import io.imoji.sdk.objects.Imoji;
import io.imoji.sdk.response.ImojisResponse;

/**
 * Imoji Android SDK
 *
 * Created by nkhoshini on 2/25/16.
 */
public class ImojiResultsDeserializer implements JsonDeserializer<ImojisResponse> {

    @Override
    public ImojisResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject root = json.getAsJsonObject();

        List<Imoji> imojis = new ArrayList<>();
        if (root.has("results")) {
            JsonArray results = root.getAsJsonArray("results");
            for (JsonElement result : results) {
                imojis.add(context.<Imoji>deserialize(result, Imoji.class));
            }
        }

        String followupSearchTerm = null;
        if (root.has("followupSearchTerm")) {
            followupSearchTerm = root.get("followupSearchTerm").getAsString();
        }

        List<Category> relatedCategories = new ArrayList<>();
        if (root.has("relatedCategories")) {
            JsonArray categoriesJSON = root.getAsJsonArray("relatedCategories");
            for (JsonElement result : categoriesJSON) {
                relatedCategories.add(context.<Category>deserialize(result, Category.class));
            }
        }

        return new ImojisResponse(imojis, followupSearchTerm, relatedCategories);
    }
}
