/* Copyright 2008 Neo Technology, http://neotechnology.com.
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
package org.qi4j.entity.neo4j;

import org.qi4j.entity.EntityComposite;
import org.qi4j.property.Property;

/**
 * TODO: Find out the apropriate way to set up configurations for an EntityStore, then use this in EmbeddedNeoMixin.
 *
 * @author Tobias Ivarsson (tobias.ivarsson@neotechnology.com)
 */
public interface NeoCoreConfiguration extends EntityComposite
{
    /**
     * Configuration of where the Neo Nodespace is stored on disk.
     *
     * @return The path to where the Neo Nodespace is stored.
     */
    Property<String> path();

    /**
     * Configuration of what kind of index to use for indexing of entity ids.
     *
     * @return <code>true</code> if Lucene should be used to index entity ids,
     *         <code>false</code> to use a tree based index of Nodes in Neo.
     */
    Property<Boolean> useLuceneIndex();
}