package com.pahimar.ee3.util.serialize;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import net.minecraft.item.ItemStack;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.pahimar.ee3.exchange.WrappedStack;
import com.pahimar.ee3.knowledge.PlayerKnowledge;
import com.pahimar.ee3.reference.Comparators;

public class PlayerKnowledgeSerializer implements JsonSerializer<PlayerKnowledge>, JsonDeserializer<PlayerKnowledge> {

    @Override
    public PlayerKnowledge deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
        throws JsonParseException {

        Set<ItemStack> knownItemStacks = new TreeSet<>(Comparators.ID_COMPARATOR);

        if (json.isJsonArray()) {

            JsonArray jsonArray = json.getAsJsonArray();
            Iterator<JsonElement> jsonArrayIterator = jsonArray.iterator();

            while (jsonArrayIterator.hasNext()) {

                JsonElement jsonElement = jsonArrayIterator.next();

                if (jsonElement.isJsonObject()) {

                    WrappedStack wrappedStack = null;

                    try {
                        wrappedStack = context.deserialize(jsonElement, WrappedStack.class);
                    } catch (JsonParseException e) {}

                    if (wrappedStack != null && wrappedStack.getWrappedObject() instanceof ItemStack) {
                        knownItemStacks.add((ItemStack) wrappedStack.getWrappedObject());
                    }
                }
            }
        }

        return new PlayerKnowledge(knownItemStacks);
    }

    @Override
    public JsonElement serialize(PlayerKnowledge src, Type typeOfSrc, JsonSerializationContext context) {

        JsonArray jsonArray = new JsonArray();

        if (src != null) {
            for (ItemStack itemStack : src.getKnownItemStacks()) {
                jsonArray.add(context.serialize(WrappedStack.wrap(itemStack)));
            }
        }

        return jsonArray;
    }
}
