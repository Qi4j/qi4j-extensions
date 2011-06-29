/*
 * Copyright 2008 Alin Dreghiciu.
 *
 * Licensed  under the  Apache License,  Version 2.0  (the "License");
 * you may not use  this file  except in  compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under the  License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS  OF ANY KIND, either  express  or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.qi4j.index.rdf.query;

import org.openrdf.query.QueryLanguage;
import org.qi4j.api.composite.Composite;
import org.qi4j.api.entity.Entity;
import org.qi4j.api.entity.EntityReference;
import org.qi4j.api.injection.scope.Service;
import org.qi4j.api.injection.scope.This;
import org.qi4j.api.mixin.Mixins;
import org.qi4j.api.query.grammar.OrderBy;
import org.qi4j.api.query.grammar2.QuerySpecification;
import org.qi4j.api.service.ServiceComposite;
import org.qi4j.api.specification.Specification;
import org.qi4j.spi.query.EntityFinder;
import org.qi4j.spi.query.EntityFinderException;

import java.util.Map;

/**
 * JAVADOC Add JavaDoc
 */
@Mixins( { RdfQueryService.RdfEntityFinderMixin.class } )
public interface RdfQueryService
    extends EntityFinder, RdfQueryParserFactory, ServiceComposite
{
    /**
     * JAVADOC Add JavaDoc
     */
    public static class RdfEntityFinderMixin
        implements EntityFinder
    {

        private static final QueryLanguage language = QueryLanguage.SPARQL;

        @Service
        private RdfQueryParserFactory queryParserFactory;

        @This
        TupleQueryExecutor tupleExecutor;

        public Iterable<EntityReference> findEntities( Class<?> resultType,
                                                       Specification<Composite> whereClause,
                                                       OrderBy[] orderBySegments,
                                                       Integer firstResult,
                                                       Integer maxResults,
                                                       Map<String, Object> variables
        )
            throws EntityFinderException
        {
            CollectingQualifiedIdentityResultCallback collectingCallback = new CollectingQualifiedIdentityResultCallback();

            if( QuerySpecification.isQueryLanguage( "SERQL", whereClause ))
            {
                String query = ((QuerySpecification)whereClause).getQuery();
                tupleExecutor.performTupleQuery( QueryLanguage.SERQL, query, variables, collectingCallback );
                return collectingCallback.getEntities();

            } else
            {
                RdfQueryParser rdfQueryParser = queryParserFactory.newQueryParser( language );
                String query = rdfQueryParser.getQuery( resultType, whereClause, orderBySegments, firstResult, maxResults, variables );

                tupleExecutor.performTupleQuery( language, query, variables, collectingCallback );
                return collectingCallback.getEntities();
            }
        }

        public EntityReference findEntity( Class<?> resultType, Specification<Composite> whereClause, Map<String, Object> variables )
            throws EntityFinderException
        {
            final SingleQualifiedIdentityResultCallback singleCallback = new SingleQualifiedIdentityResultCallback();

            if (QuerySpecification.isQueryLanguage( "SERQL", whereClause))
            {
                String query = ((QuerySpecification)whereClause).getQuery();
                tupleExecutor.performTupleQuery( QueryLanguage.SERQL, query, variables, singleCallback );
                return singleCallback.getQualifiedIdentity();
            } else
            {
                RdfQueryParser rdfQueryParser = queryParserFactory.newQueryParser( language );
                String query = rdfQueryParser.getQuery( resultType, whereClause, null, null, null, variables );
                tupleExecutor.performTupleQuery( QueryLanguage.SPARQL, query, variables, singleCallback );
                return singleCallback.getQualifiedIdentity();
            }
        }

        public long countEntities( Class<?> resultType, Specification<Composite> whereClause, Map<String, Object> variables )
            throws EntityFinderException
        {
            if (QuerySpecification.isQueryLanguage( "SERQL", whereClause ))
            {
                String query = ((QuerySpecification)whereClause).getQuery();
                return tupleExecutor.performTupleQuery( QueryLanguage.SERQL, query, variables, null );

            } else
            {
                RdfQueryParser rdfQueryParser = queryParserFactory.newQueryParser( language );
                String query = rdfQueryParser.getQuery( resultType, whereClause, null, null, null, variables );
                return tupleExecutor.performTupleQuery( language, query, variables, null );
            }
        }
    }
}
