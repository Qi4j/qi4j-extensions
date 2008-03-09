/*  Copyright 2007 Niclas Hedhman.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 * 
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.qi4j.entity.memory;

import java.util.Map;
import org.qi4j.association.AbstractAssociation;
import org.qi4j.entity.EntityComposite;
import org.qi4j.property.Property;
import org.qi4j.spi.entity.EntityState;
import org.qi4j.spi.entity.StoreException;
import org.qi4j.spi.entity.EntityStore;
import org.qi4j.spi.serialization.SerializedEntity;
import org.qi4j.spi.composite.CompositeBinding;
import org.qi4j.spi.composite.CompositeResolution;
import org.qi4j.spi.composite.CompositeModel;

public class MemoryEntityState
    implements EntityState
{
    private String identity;
    private final CompositeBinding compositeBinding;
    private Map<String, Property> properties;
    private Map<String, AbstractAssociation> associations;
    private MemoryEntityStore owningStore;

    public MemoryEntityState( String identity, CompositeBinding compositeBinding, Map<String, Property> properties, Map<String, AbstractAssociation> associations, MemoryEntityStore owningStore )
    {
        this.identity = identity;
        this.compositeBinding = compositeBinding;
        this.properties = properties;
        this.associations = associations;
        this.owningStore = owningStore;
    }

    public String getIdentity()
    {
        return identity;
    }

    public CompositeBinding getCompositeBinding()
    {
        return compositeBinding;
    }

    public Map<String, Property> getProperties()
    {
        return properties;
    }

    public Map<String, AbstractAssociation> getAssociations()
    {
        return associations;
    }

    public void refresh()
    {
    }

    public boolean delete()
        throws StoreException
    {
        CompositeResolution compositeResolution = compositeBinding.getCompositeResolution();
        CompositeModel compositeModel = compositeResolution.getCompositeModel();
        Class<? extends EntityComposite> compositeType = (Class<? extends EntityComposite>) compositeModel.getCompositeClass();
        return owningStore.remove( new SerializedEntity( identity, compositeType ) );
    }
}