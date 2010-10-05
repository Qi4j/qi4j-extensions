/*
 * Copyright (c) 2010, Stanislav Muhametsin. All Rights Reserved.
 * Copyright (c) 2010, Paul Merlin. All Rights Reserved.
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
package org.qi4j.entitystore.sql.assembly;

import org.qi4j.api.common.Visibility;
import org.qi4j.entitystore.sql.internal.PostgreSQLDatabaseSQLServiceMixin;
import org.qi4j.library.sql.ds.PGSQLDataSourceServiceMixin;
import org.qi4j.library.sql.ds.assembly.DataSourceAssembler;

public class PostgreSQLEntityStoreAssembler extends AbstractSQLEntityStoreAssembler
{

    public static final String ENTITYSTORE_SERVICE_NAME = "entitystore-postgresql";

    public static final String DATASOURCE_SERVICE_NAME = "datasource-postgresql";

    public PostgreSQLEntityStoreAssembler()
    {
        super( new DataSourceAssembler().setDataSourceServiceName( DATASOURCE_SERVICE_NAME ) );
    }

    public PostgreSQLEntityStoreAssembler( Visibility visibility )
    {
        super( visibility, new DataSourceAssembler( PGSQLDataSourceServiceMixin.class )
            .setDataSourceServiceName( DATASOURCE_SERVICE_NAME ) );
    }

    public PostgreSQLEntityStoreAssembler( DataSourceAssembler assembler )
    {
        super( assembler );
    }

    public PostgreSQLEntityStoreAssembler( Visibility visibility, DataSourceAssembler assembler )
    {
        super( visibility, assembler );
    }

    @Override
    protected String getEntityStoreServiceName()
    {
        return ENTITYSTORE_SERVICE_NAME;
    }

    @Override
    protected Class<?> getDatabaseSQLServiceSpecializationMixin()
    {
        return PostgreSQLDatabaseSQLServiceMixin.class;
    }

}