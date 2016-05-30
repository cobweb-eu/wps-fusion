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

import de.tudresden.gis.fusion.data.IData;
import de.tudresden.gis.fusion.data.binding.FeatureRelationBinding;
import de.tudresden.gis.fusion.data.binding.GTFeatureCollectionBinding;
import de.tudresden.gis.fusion.data.feature.geotools.GTFeatureCollection;
import de.tudresden.gis.fusion.data.feature.geotools.GTGridCoverage;
import de.tudresden.gis.fusion.data.literal.BooleanLiteral;
import de.tudresden.gis.fusion.data.literal.DecimalLiteral;
import de.tudresden.gis.fusion.data.literal.IntegerLiteral;
import de.tudresden.gis.fusion.data.literal.LongLiteral;
import de.tudresden.gis.fusion.data.literal.StringLiteral;
import de.tudresden.gis.fusion.data.literal.URILiteral;
import de.tudresden.gis.fusion.data.relation.FeatureRelationCollection;
import de.tudresden.gis.fusion.operation.constraint.BindingConstraint;
import de.tudresden.gis.fusion.operation.constraint.IDataConstraint;
import de.tudresden.gis.fusion.operation.description.IIODataDescription;

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
			return new GTFeatureCollection(data.toString(), (SimpleFeatureCollection)((GTVectorDataBinding) data).getPayload());
		if(data instanceof GTFeatureCollectionBinding)
			return (GTFeatureCollection) ((GTFeatureCollectionBinding) data).getPayload();
		if(data instanceof GTRasterDataBinding)
			return new GTGridCoverage(data.toString(), (GridCoverage2D)((GTRasterDataBinding) data).getPayload());
		if(data instanceof FeatureRelationBinding)
			return ((FeatureRelationBinding) data).getPayload();
		if(data instanceof LiteralAnyURIBinding)
			return new URILiteral(((LiteralAnyURIBinding) data).getPayload());
		
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
			return new LiteralIntBinding(((IntegerLiteral) data).resolve());
		if(data instanceof LongLiteral)
			return new LiteralLongBinding(((LongLiteral) data).resolve());
		if(data instanceof DecimalLiteral)
			return new LiteralDoubleBinding(((DecimalLiteral) data).resolve());
		if(data instanceof BooleanLiteral)
			return new LiteralBooleanBinding(((BooleanLiteral) data).resolve());
		if(data instanceof StringLiteral)
			return new LiteralStringBinding(((StringLiteral) data).resolve());
		if(data instanceof GTFeatureCollection)
			return new GTFeatureCollectionBinding((GTFeatureCollection) data);
		if(data instanceof GTGridCoverage)
			return new GTRasterDataBinding(((GTGridCoverage) data).resolve());
		if(data instanceof FeatureRelationCollection)
			return new FeatureRelationBinding((FeatureRelationCollection) data);
		if(data instanceof URILiteral)
			return new LiteralAnyURIBinding(((URILiteral) data).resolve());
		
		return null;
	}
	
	/**
	 * get supported binding for fusion io description
	 * @param description fusion io description
	 * @return supported framework binding
	 */
	public static Class<?> getSupportedClass(IIODataDescription description){
		
		for(IDataConstraint constraint : description.constraints()){
			if(constraint instanceof BindingConstraint)
				return getSupportedClass((BindingConstraint) constraint);
		}
		
		return null;
	}
	
	/**
	 * get supported binding based on io restriction
	 * @param restriction io restriction
	 * @return supported binding
	 */
	private static Class<?> getSupportedClass(BindingConstraint constraint){
		
		if(constraint.compliantWith(IntegerLiteral.class))
			return LiteralIntBinding.class;
		if(constraint.compliantWith(LongLiteral.class))
			return LiteralLongBinding.class;
		if(constraint.compliantWith(DecimalLiteral.class))
			return LiteralDoubleBinding.class;
		if(constraint.compliantWith(BooleanLiteral.class))
			return LiteralBooleanBinding.class;
		if(constraint.compliantWith(StringLiteral.class))
			return LiteralStringBinding.class;
		if(constraint.compliantWith(GTFeatureCollection.class))
			return GTFeatureCollectionBinding.class;
		if(constraint.compliantWith(GTGridCoverage.class))
			return GTRasterDataBinding.class;
		if(constraint.compliantWith(FeatureRelationCollection.class))
			return FeatureRelationBinding.class;
		if(constraint.compliantWith(URILiteral.class))
			return LiteralAnyURIBinding.class;
		
		return null;
	}
	
}
