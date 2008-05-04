/*
 * Copyright (c) 2008, Niclas Hedhman. All Rights Reserved.
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
package org.qi4j.quikit.pages;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.qi4j.composite.ObjectBuilder;
import org.qi4j.composite.ObjectBuilderFactory;
import org.qi4j.composite.scope.Structure;
import org.qi4j.composite.scope.Uses;
import org.qi4j.quikit.panels.EntityFormEditPanel;

public class EntityFormEditPage extends WebPage
{
    public EntityFormEditPage( @Structure ObjectBuilderFactory objectBuilderFactory )
    {
        ObjectBuilder<EntityFormEditPanel> builder = objectBuilderFactory.newObjectBuilder( EntityFormEditPanel.class );
        builder.use( "edit-panel" );
        EntityFormEditPanel panel = builder.newInstance();
        add( panel );
    }
}