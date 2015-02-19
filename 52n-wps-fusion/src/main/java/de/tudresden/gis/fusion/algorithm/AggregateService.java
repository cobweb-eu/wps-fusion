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
package de.tudresden.gis.fusion.algorithm;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.n52.wps.algorithm.annotation.ComplexDataInput;
import org.n52.wps.algorithm.annotation.ComplexDataOutput;
import org.n52.wps.io.data.binding.complex.GTVectorDataBinding;
import org.n52.wps.server.AbstractAnnotatedAlgorithm;

import de.tudresden.gis.fusion.data.IFeatureRelationCollection;
import de.tudresden.gis.fusion.data.binding.IFeatureRelationBinding;

public class AggregateService extends AbstractAnnotatedAlgorithm {
	
	//input identifier
	private final String IN_REFERENCE = "IN_REFERENCE";
	private final String IN_TARGET = "IN_TARGET";
	
	//input data
	private SimpleFeatureCollection inReference;
	private SimpleFeatureCollection inTarget;
	
	//output identifier
	private final String OUT_RELATIONS = "OUT_RELATIONS";
	
	//output data
	private IFeatureRelationCollection relations;
	
	@ComplexDataInput(identifier=IN_REFERENCE, title="reference features", binding=GTVectorDataBinding.class, minOccurs=1, maxOccurs=1)
    public void setReference(FeatureCollection<?,?> inReference) {
        this.inReference = (SimpleFeatureCollection) inReference;
    }
    
    @ComplexDataInput(identifier=IN_TARGET, title="target features", binding=GTVectorDataBinding.class, minOccurs=1, maxOccurs=1)
    public void setTarget(FeatureCollection<?,?> inTarget) {
        this.inTarget = (SimpleFeatureCollection) inTarget;
    }
	
	@ComplexDataOutput(identifier=OUT_RELATIONS, title="relations between reference and target features", binding=IFeatureRelationBinding.class)
	public IFeatureRelationCollection getRelations() {
		return relations;
	}
	
}
