/*
 * Copyright (c) 2011, Niclas Hedhman. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.qi4j.entitystore.neo4j;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.qi4j.api.entity.EntityReference;
import org.qi4j.spi.entity.NamedAssociationState;
import org.qi4j.spi.entity.association.NamedEntityReference;

public class NeoNamedAssociationState
    implements NamedAssociationState
{
    static final String COUNT = "count";

    private final Node underlyingNode;
    private final NeoEntityState entity;
    private final NeoEntityStoreUnitOfWork uow;

    NeoNamedAssociationState( NeoEntityStoreUnitOfWork uow,
                              NeoEntityState entity, Node node
    )
    {
        this.uow = uow;
        this.entity = entity;
        this.underlyingNode = node;
    }

    public void put( String name, EntityReference entityReference )
    {
        Node entityNode = uow.getEntityStateNode( entityReference );
        DynamicRelationshipType relationshipType = DynamicRelationshipType.withName( name );
        Relationship relationship = underlyingNode.getSingleRelationship( relationshipType, Direction.OUTGOING );
        if( relationship != null )
        {
            // It already exist, do nothing.
            return;
        }
        underlyingNode.createRelationshipTo( entityNode, relationshipType );
        incrementCount();
        entity.setUpdated();
    }

    public boolean containsKey( String name )
    {
        DynamicRelationshipType relationshipType = DynamicRelationshipType.withName( name );
        Relationship relationship = underlyingNode.getSingleRelationship( relationshipType, Direction.OUTGOING );
        return relationship != null;
    }

    public String contains( EntityReference entityReference )
    {
        Node entityNode = uow.getEntityStateNode( entityReference );
        for( Relationship rel : underlyingNode.getRelationships( Direction.OUTGOING ) )
        {
            if( rel.getEndNode().equals( entityNode ) )
            {
                return rel.getType().name();
            }
        }
        return null;
    }

    public int count()
    {
        return (Integer) underlyingNode.getProperty( COUNT );
    }

    public EntityReference get( String name )
    {
        DynamicRelationshipType relationshipType = DynamicRelationshipType.withName( name );
        Relationship relationship = underlyingNode.getSingleRelationship( relationshipType, Direction.OUTGOING );
        if( relationship == null )
        {
            return null;
        }
        String id = (String) relationship.getEndNode().getProperty( NeoEntityState.ENTITY_ID );
        return new EntityReference( id );
    }

    @Override
    public Iterable<String> names()
    {
        ArrayList<String> result = new ArrayList<String>();
        for( Relationship relationship : underlyingNode.getRelationships( Direction.OUTGOING ) )
        {
            result.add( relationship.getType().name() );
        }
        return result;
    }

    public boolean remove( String name )
    {
        DynamicRelationshipType relationshipType = DynamicRelationshipType.withName( name );
        Relationship relationship = underlyingNode.getSingleRelationship( relationshipType, Direction.OUTGOING );
        if( relationship != null )
        {
            relationship.delete();
            decrementCount();
            entity.setUpdated();
        }
        return true;
    }

    public Iterator<NamedEntityReference> iterator()
    {
        List<NamedEntityReference> list = new ArrayList<NamedEntityReference>( count() );
        for( Relationship rel : underlyingNode.getRelationships( Direction.OUTGOING ) )
        {
            String name = rel.getType().name();
            String id = (String) rel.getEndNode().getProperty( NeoEntityState.ENTITY_ID );
            list.add( new NamedEntityReference( name, new EntityReference( id ) ) );
        }
        return list.iterator();
    }

    private void incrementCount()
    {
        int count = (Integer) underlyingNode.getProperty( "count" );
        underlyingNode.setProperty( COUNT, count + 1 );
    }

    private void decrementCount()
    {
        int count = (Integer) underlyingNode.getProperty( "count" );
        underlyingNode.setProperty( COUNT, --count );
    }
}