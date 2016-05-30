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

import org.geotools.feature.FeatureCollection;
import org.n52.wps.algorithm.annotation.Algorithm;
import org.n52.wps.algorithm.annotation.ComplexDataInput;
import org.n52.wps.algorithm.annotation.ComplexDataOutput;
import org.n52.wps.algorithm.annotation.Execute;
import org.n52.wps.io.data.binding.complex.GTVectorDataBinding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Algorithm(abstrakt="Determines distance relation between input features", version="1.0")
public class COBWEB_SecureDummy extends COBWEB_Algorithm {
	
	private static Logger LOGGER = LoggerFactory.getLogger(COBWEB_SecureDummy.class);
	
	//input identifier
	private final String IN_REFERENCE = "IN_REFERENCE";
	
	//input data
	private FeatureCollection inReference;
	
	//output identifier
	private final String OUT_REFERENCE = "OUT_REFERENCE";
	
	//output data
	private FeatureCollection outReference;

	//constructor
    public COBWEB_SecureDummy() {
        super();
    }

    @ComplexDataInput(identifier=IN_REFERENCE, title="reference features", binding=GTVectorDataBinding.class, minOccurs=1, maxOccurs=1)
    public void setReference(FeatureCollection inReference) {
        this.inReference = inReference;
    }
    
	@ComplexDataOutput(identifier=OUT_REFERENCE, title="reference features", binding=GTVectorDataBinding.class)
    public FeatureCollection getReference() {
        return outReference;
    }
    
    @Execute
    public void execute() {    	
    	LOGGER.info("Number of reference features: " + inReference.size());		
		outReference = inReference;
    }

}
