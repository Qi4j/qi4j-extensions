/*
 * Copyright (c) 2009, Rickard Öberg. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.qi4j.migration.operation;

import org.qi4j.migration.assembly.MigrationOperation;
import org.qi4j.spi.util.json.JSONObject;
import org.qi4j.spi.util.json.JSONException;
import org.qi4j.entitystore.map.MapEntityStore;
import org.qi4j.entitystore.map.StateStore;

/**
 * Rename a property
 */
public class RenameProperty
    implements MigrationOperation
{
    String fromProperty;
    String toProperty;

    public RenameProperty( String fromProperty, String toProperty )
    {
        this.fromProperty = fromProperty;
        this.toProperty = toProperty;
    }

    public boolean upgrade( JSONObject state, StateStore stateStore ) throws JSONException
    {
        JSONObject properties = (JSONObject) state.get( MapEntityStore.JSONKeys.properties.name() );

        Object value = properties.remove( fromProperty );
        properties.put( toProperty, value );

        return true;
    }

    public boolean downgrade( JSONObject state, StateStore stateStore ) throws JSONException
    {
        JSONObject properties = (JSONObject) state.get( MapEntityStore.JSONKeys.properties.name() );

        Object value = properties.remove( toProperty );
        properties.put( fromProperty, value );

        return true;
    }

    @Override public String toString()
    {
        return "Rename property "+fromProperty+" to "+toProperty;
    }
}
