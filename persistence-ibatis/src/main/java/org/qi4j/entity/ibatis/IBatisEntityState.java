/*  Copyright 2008 Edward Yakop.
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
package org.qi4j.entity.ibatis;

import java.util.Map;
import org.qi4j.association.AbstractAssociation;
import static org.qi4j.composite.NullArgumentException.validateNotNull;
import static org.qi4j.entity.ibatis.IBatisEntityState.STATUS.statusLoadToDeleted;
import static org.qi4j.entity.ibatis.IBatisEntityState.STATUS.statusNew;
import static org.qi4j.entity.ibatis.IBatisEntityState.STATUS.statusNewToDeleted;
import org.qi4j.property.Property;
import org.qi4j.spi.composite.CompositeBinding;
import org.qi4j.spi.entity.EntityState;
import org.qi4j.spi.entity.StoreException;

/**
 * {@code IBatisEntityState} represents {@code IBatis} version of {@link EntityState}.
 *
 * @author edward.yakop@gmail.com
 * @since 0.1.0
 */
final class IBatisEntityState
    implements EntityState
{
    public static enum STATUS
    {
        statusNew,
        statusLoadFromDb,
        statusNewToDeleted,
        statusLoadToDeleted,
    }

    private final String identity;

    private final CompositeBinding compositeBinding;
    private final Map<String, Property> properties;
    private final Map<String, AbstractAssociation> associations;
    private final IBatisEntityStore entityStore;
    private STATUS status;

    /**
     * Construct an instance of {@code IBatisEntityState}.
     *
     * @param anIdentity        The identity. This argument must not be {@code null}.
     * @param aCompositeBinding The composite binding. This argument must not be {@code null}.
     * @param propertiez        The entity properties. This argument must not be {@code null}.
     * @param associationz      The entity associations. This argument must not be {@code null}.
     * @param aStatus           The initial status of this state. This argument must not be {@code null}.
     * @param anEntitySTore     The entity store. This argument must not be {@code null}.
     * @throws IllegalArgumentException Thrown if one or some or all arguments are {@code null}.
     * @since 0.1.0
     */
    IBatisEntityState(
        String anIdentity, CompositeBinding aCompositeBinding,
        Map<String, Property> propertiez,
        Map<String, AbstractAssociation> associationz,
        STATUS aStatus, IBatisEntityStore anEntitySTore )
        throws IllegalArgumentException
    {
        validateNotNull( "anIdentity", anIdentity );
        validateNotNull( "aCompositeBinding", aCompositeBinding );
        validateNotNull( "propertiez", propertiez );
        validateNotNull( "associationz", associationz );
        validateNotNull( "aStatus", aStatus );
        validateNotNull( "anEntitySTore", anEntitySTore );

        identity = anIdentity;
        compositeBinding = aCompositeBinding;
        properties = propertiez;
        associations = associationz;
        entityStore = anEntitySTore;
        status = aStatus;
    }

    /**
     * Returns the identity of the entity that this EntityState represents.
     *
     * @return the identity of the entity that this EntityState represents.
     * @since 0.1.0
     */
    public final String getIdentity()
    {
        return identity;
    }

    public final CompositeBinding getCompositeBinding()
    {
        return compositeBinding;
    }

    public final Map<String, Property> getProperties()
    {
        return properties;
    }

    public final Map<String, AbstractAssociation> getAssociations()
    {
        return associations;
    }

    public final void refresh()
    {
        // Check whether refresh is required at all
        if( status == statusNew || status == statusNewToDeleted || status == statusLoadToDeleted )
        {
            return;
        }

        // TODO
    }

    public boolean delete()
        throws StoreException
    {
        switch( status )
        {
        case statusNew:
        case statusNewToDeleted:
        case statusLoadToDeleted:
            status = statusNewToDeleted;
            return true;

        case statusLoadFromDb:
            return entityStore.deleteComposite( identity, compositeBinding );
        }

        return false;
    }

    final void persist()
    {
        // TODO
    }
}