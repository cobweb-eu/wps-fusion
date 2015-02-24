/**
 * ﻿Copyright (C) 2014 - 2014 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published
 * by the Free Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of
 * the following licenses, the combination of the program with the linked
 * library is not considered a "derivative work" of the program:
 *
 *       • Apache License, version 2.0
 *       • Apache Software License, version 1.0
 *       • GNU Lesser General Public License, version 3
 *       • Mozilla Public License, versions 1.0, 1.1 and 2.0
 *       • Common Development and Distribution License (CDDL), version 1.0
 *
 * Therefore the distribution of the program linked with libraries licensed
 * under the aforementioned licenses, is permitted by the copyright holders
 * if the distribution is compliant with both the GNU General Public
 * License version 2 and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 */
package de.tudresden.gis.fusion;

import java.util.List;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.n52.wps.io.data.binding.complex.GTRasterDataBinding;
import org.n52.wps.io.data.binding.complex.GTVectorDataBinding;
import org.n52.wps.io.data.binding.literal.LiteralAnyURIBinding;
import org.n52.wps.io.data.binding.literal.LiteralBooleanBinding;
import org.n52.wps.io.data.binding.literal.LiteralDoubleBinding;
import org.n52.wps.io.data.binding.literal.LiteralIntBinding;
import org.n52.wps.io.data.binding.literal.LiteralLongBinding;
import org.n52.wps.io.data.binding.literal.LiteralStringBinding;

import de.tudresden.gis.fusion.data.ICoverage;
import de.tudresden.gis.fusion.data.IData;
import de.tudresden.gis.fusion.data.IDataResource;
import de.tudresden.gis.fusion.data.IFeatureCollection;
import de.tudresden.gis.fusion.data.IFeatureRelationCollection;
import de.tudresden.gis.fusion.data.binding.FeatureRelationBinding;
import de.tudresden.gis.fusion.data.binding.GTFeatureCollectionBinding;
import de.tudresden.gis.fusion.data.geotools.GTFeatureCollection;
import de.tudresden.gis.fusion.data.geotools.GTGridCoverage2D;
import de.tudresden.gis.fusion.data.rdf.IRI;
import de.tudresden.gis.fusion.data.rdf.Resource;
import de.tudresden.gis.fusion.data.restrictions.JavaBindingRestriction;
import de.tudresden.gis.fusion.data.simple.BooleanLiteral;
import de.tudresden.gis.fusion.data.simple.DecimalLiteral;
import de.tudresden.gis.fusion.data.simple.IntegerLiteral;
import de.tudresden.gis.fusion.data.simple.LongLiteral;
import de.tudresden.gis.fusion.data.simple.StringLiteral;
import de.tudresden.gis.fusion.operation.io.IDataRestriction;
import de.tudresden.gis.fusion.operation.metadata.IIODescription;

public class DataTransformer {
	
	/**
	 * transform IData object from 52n to fusion domain
	 * @param data 52n IData object
	 * @return fusion IData object
	 */
	public static IData transformIData(org.n52.wps.io.data.IData data){
		
		if(data instanceof LiteralIntBinding)
			return new IntegerLiteral(((LiteralIntBinding) data).getPayload());
		if(data instanceof LiteralLongBinding)
			return new LongLiteral(((LiteralLongBinding) data).getPayload());
		if(data instanceof LiteralDoubleBinding)
			return new DecimalLiteral(((LiteralDoubleBinding) data).getPayload());
		if(data instanceof LiteralBooleanBinding)
			return new BooleanLiteral(((LiteralBooleanBinding) data).getPayload());
		if(data instanceof LiteralStringBinding)
			return new StringLiteral(((LiteralStringBinding) data).getPayload());
		if(data instanceof GTVectorDataBinding)
			return new GTFeatureCollection(new IRI(data.toString()), (SimpleFeatureCollection)((GTVectorDataBinding) data).getPayload());
		if(data instanceof GTFeatureCollectionBinding)
			return (GTFeatureCollection) ((GTFeatureCollectionBinding) data).getPayload();
		if(data instanceof GTRasterDataBinding)
			return new GTGridCoverage2D(new IRI(data.toString()), (GridCoverage2D)((GTRasterDataBinding) data).getPayload());
		if(data instanceof FeatureRelationBinding)
			return ((FeatureRelationBinding) data).getPayload();
		if(data instanceof LiteralAnyURIBinding)
			return new Resource(new IRI(((LiteralAnyURIBinding) data).getPayload()));
		
		return null;
	}
	
	/**
	 * transform IData list object from 52n to fusion domain
	 * @param data 52n IData list object
	 * @return fusion IData object (only first item if list object!)
	 */
	public static IData transformIData(List<org.n52.wps.io.data.IData> list){
		return transformIData(list.get(0));
	}
	
	/**
	 * transform IData object from fusion to 52n domain
	 * @param data fusion IData object
	 * @return 52n IData object
	 */
	public static org.n52.wps.io.data.IData transformIData(IData data){
		
		if(data instanceof IntegerLiteral)
			return new LiteralIntBinding(((IntegerLiteral) data).getValue());
		if(data instanceof LongLiteral)
			return new LiteralLongBinding(((LongLiteral) data).getValue());
		if(data instanceof DecimalLiteral)
			return new LiteralDoubleBinding(((DecimalLiteral) data).getValue());
		if(data instanceof BooleanLiteral)
			return new LiteralBooleanBinding(((BooleanLiteral) data).getValue());
		if(data instanceof StringLiteral)
			return new LiteralStringBinding(((StringLiteral) data).getValue());
		if(data instanceof GTFeatureCollection)
			return new GTFeatureCollectionBinding((GTFeatureCollection) data);
		if(data instanceof GTGridCoverage2D)
			return new GTRasterDataBinding(((GTGridCoverage2D) data).getCoverage());
		if(data instanceof IFeatureRelationCollection)
			return new FeatureRelationBinding((IFeatureRelationCollection) data);
		if(data instanceof IDataResource)
			return new LiteralAnyURIBinding(((IDataResource) data).getIdentifier().asURI());
		
		return null;
	}
	
	/**
	 * get supported binding for fusion io description
	 * @param description fusion io description
	 * @return supported framework binding
	 */
	public static Class<?> getSupportedClass(IIODescription description){
		
		for(IDataRestriction restriction : description.getDataRestrictions()){
			if(restriction instanceof JavaBindingRestriction)
				return getSupportedClass((JavaBindingRestriction) restriction);
		}
		
		return null;
	}
	
	/**
	 * get supported binding based on io restriction
	 * @param restriction io restriction
	 * @return supported binding
	 */
	private static Class<?> getSupportedClass(JavaBindingRestriction restriction){
		
		if(restriction.compliantWith(IntegerLiteral.class))
			return LiteralIntBinding.class;
		if(restriction.compliantWith(LongLiteral.class))
			return LiteralLongBinding.class;
		if(restriction.compliantWith(DecimalLiteral.class))
			return LiteralDoubleBinding.class;
		if(restriction.compliantWith(BooleanLiteral.class))
			return LiteralBooleanBinding.class;
		if(restriction.compliantWith(StringLiteral.class))
			return LiteralStringBinding.class;
		if(restriction.compliantWith(IFeatureCollection.class))
			return GTFeatureCollectionBinding.class;
		if(restriction.compliantWith(ICoverage.class))
			return GTRasterDataBinding.class;
		if(restriction.compliantWith(IFeatureRelationCollection.class))
			return FeatureRelationBinding.class;
		if(restriction.compliantWith(IDataResource.class))
			return LiteralAnyURIBinding.class;
		
		return null;
	}
	
}
