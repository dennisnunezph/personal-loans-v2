/**
 * Copyright (c) 2012-2013 Edgar Espina
 *
 * This file is part of Handlebars.java.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.jknack.handlebars.helper;

import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * You can iterate over a list using the built-in each helper. Inside the
 * block, you can use <code>this</code> to reference the element being
 * iterated over.
 *
 * @author edgar.espina
 * @since 0.3.0
 */
public class DefaultFilterHelper implements Helper<Object> {

  /**
   * A singleton instance of this helper.
   */
  public static final Helper<Object> INSTANCE = new DefaultFilterHelper();

  /**
   * The helper's name.
   */
  public static final String NAME = "defaultFilter";
  private static final String TYPEVALUE = "typeValue";
  private static final String FILTER = "filter";

  @SuppressWarnings({"rawtypes", "unchecked" })
  @Override
  public CharSequence apply(final Object context, final Options options)
      throws IOException {
    if (context == null) {
      return StringUtils.EMPTY;
    }
    if (context instanceof Iterable) {
        String[] filterValues = null;
        String filterValue = "";
        String targetField = "";
        if (options.hash.size() > 0) {
            for(Map.Entry<String, Object> item : options.hash.entrySet()) {
                if (item != null) {
                    filterValues = StringUtils.defaultString(item.getValue().toString()).split(",");
                    if (filterValues != null
                            && filterValues.length > 0) {
                        filterValue = StringUtils.defaultString(filterValues[0]).trim();

                        if (filterValues.length > 1) {
                            targetField = StringUtils.defaultString(filterValues[1]).trim();
                        }
                    }
                    break;
                }
            }
        }
        if (filterValues != null
                && filterValues.length > 0) {
            Set<Entry<String, Object>> propertySet = options.propertySet(context);
            Iterator<Entry<String, Object>> contextLoop = ((Iterable) context).iterator();
            while (contextLoop.hasNext()) {
                Object item = contextLoop.next();
                if (item != null) {
                  String typeValue =  getFieldValue(item, TYPEVALUE);
                  boolean matched = StringUtils.isNotBlank(typeValue)
                          && filterValue.equalsIgnoreCase(typeValue);
                  if (matched) {
                        return getFieldValue(item, targetField);
                  }
                }
            }
        }
    }
    return hashContext(context, options);
  }

  /**
   * Iterate over a hash like object.
   *
   * @param context The context object.
   * @param options The helper options.
   * @return The string output.
   * @throws IOException If something goes wrong.
   */
  private CharSequence hashContext(final Object context, final Options options)
      throws IOException {
    Set<Entry<String, Object>> propertySet = options.propertySet(context);
    StringBuilder buffer = new StringBuilder();
    Context parent = options.context;
    boolean first = true;
    for (Entry<String, Object> entry : propertySet) {
      Context current = Context.newBuilder(parent, entry.getValue())
          .combine("@key", entry.getKey())
          .combine("@first", first ? "first" : "")
          .build();
      buffer.append(options.fn(current));
      first = false;
    }
    return buffer.toString();
  }

  /**
   * Iterate over an iterable object.
   *
   * @param context The context object.
   * @param options The helper options.
   * @return The string output.
   * @throws IOException If something goes wrong.
   */
  private CharSequence iterableContext(final Iterable<Object> context, final Options options)
      throws IOException {
    StringBuilder buffer = new StringBuilder();
    if (options.isFalsy(context)) {
      buffer.append(options.inverse());
    } else {
      Iterator<Object> iterator = context.iterator();
      int index = -1;
      Context parent = options.context;
      while (iterator.hasNext()) {
        index += 1;
        Object element = iterator.next();
        boolean first = index == 0;
        boolean even = index % 2 == 0;
        boolean last = !iterator.hasNext();
        Context current = Context.newBuilder(parent, element)
            .combine("@index", index)
            .combine("@first", first ? "first" : "")
            .combine("@last", last ? "last" : "")
            .combine("@odd", even ? "" : "odd")
            .combine("@even", even ? "even" : "")
            // 1-based index
            .combine("@index_1", index + 1)
            .build();
        buffer.append(options.fn(current));
        current.destroy();
      }
    }
    return buffer.toString();
  }

  private static  <T> String getFieldValue(T input, String fielName) {
        String typeVal = "";
        try {
            Class<T> clazz = (Class<T>) input.getClass();
            Field field = clazz.getDeclaredField(fielName);
            field.setAccessible(true);
            typeVal = (String) field.get(input);
        } catch (Exception ex) {

        }
        return typeVal;
  }

}
