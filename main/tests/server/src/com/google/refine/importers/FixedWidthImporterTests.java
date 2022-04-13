/*******************************************************************************
 * Copyright (C) 2018, OpenRefine contributors
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 ******************************************************************************/

package com.google.refine.importers;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.refine.history.Change;
import com.google.refine.model.Column;
import com.google.refine.model.Row;
import com.google.refine.model.changes.ColumnMoveChange;
import com.google.refine.model.changes.ColumnRemovalChange;
import com.google.refine.model.changes.ColumnReorderChange;
import com.google.refine.operations.column.ColumnMoveOperation;
import com.google.refine.operations.column.ColumnRemovalOperation;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.refine.util.JSONUtilities;
import com.google.refine.util.ParsingUtilities;

public class FixedWidthImporterTests extends ImporterTest {

    @Override
    @BeforeTest
    public void init() {
        logger = LoggerFactory.getLogger(this.getClass());
    }

    // constants
    String SAMPLE_ROW = "NDB_No      Shrt_DescWater";

    // System Under Test
    FixedWidthImporter SUT = null;

    @Override
    @BeforeMethod
    public void setUp() {
        super.setUp();
        SUT = new FixedWidthImporter();
    }

    @Override
    @AfterMethod
    public void tearDown() {
        SUT = null;
        super.tearDown();
    }

    // ---------------------read tests------------------------
    @Test
    public void readFixedWidth() {
        StringReader reader = new StringReader("123456789"+"\n"+"abcdefghi"+"\n"+"\nt11y22u33");

        ArrayNode columnWidths = ParsingUtilities.mapper.createArrayNode();
        JSONUtilities.append(columnWidths, 3);
        JSONUtilities.append(columnWidths, 0);
        JSONUtilities.append(columnWidths, 3);
        JSONUtilities.append(columnWidths, 0);
        JSONUtilities.append(columnWidths, 3);
        whenGetArrayOption("columnWidths", options, columnWidths);

        ArrayNode columnNames = ParsingUtilities.mapper.createArrayNode();
        columnNames.add("Col 1");
        columnNames.add("Col 2");
        columnNames.add("Col 3");
        columnNames.add("Col 4");
        columnNames.add("Col 5");
        whenGetArrayOption("columnNames", options, columnNames);

        whenGetIntegerOption("ignoreLines", options, 0);
        whenGetIntegerOption("headerLines", options, 1);
        whenGetIntegerOption("skipDataLines", options, 0);
        whenGetIntegerOption("limit", options, -1);
        whenGetBooleanOption("storeBlankCellsAsNulls", options, true);
        whenGetBooleanOption("storeBlankRows", options, false);
        whenGetBooleanOption("storeBlankColumns", options, false);

        try {
            parseOneFile(SUT, reader);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }

        System.out.println("*****columns name");
//        List<String> columnNames2 = new ArrayList<String>();
//        columnNames2 = project.columnModel.getColumnNames();
//        for(int i=0; i<columnNames2.size(); i++) {
//            System.out.print (columnNames2.get(i)+"|");
//        }
        System.out.println("*****");
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 5; j++) {
                System.out.print(project.rows.get(i).getCellValue(j)+"|");
            }
            System.out.println();
        }
        System.out.println("*****");


        //处理
//        int index = 2;
//        for (int i = 0; i < project.rows.size(); i++) {
//            Row row = project.rows.get(i);
//            for (int j = index; j <= project.rows.size() ; j++) {
//                row.setCell(j, project.rows.get(i).getCell(j+1));
//            }
//            row.setCell(project.rows.get(i).cells.size()-1, null);
//        }

//        System.out.println("2*****");
//        for (int i = 0; i < 3; i++) {
//            for (int j = 0; j < 5; j++) {
//                System.out.print(project.rows.get(i).getCellValue(j)+"|");
//            }
//            System.out.println();
//        }
//        System.out.println(project.rows.get(1).cells.size());
//        System.out.println("2*****");
//        Assert.assertEquals(project.rows.size(), 5); // Column names count as a row?
//        Assert.assertEquals(project.rows.get(1).cells.size(), 4);
//        Assert.assertEquals((String) project.rows.get(1).getCellValue(0), "NDB_No");
//        Assert.assertEquals((String) project.rows.get(1).getCellValue(1), "Shrt_Desc");
//        Assert.assertEquals((String) project.rows.get(1).getCellValue(2), "Water");
//        Assert.assertEquals(project.rows.get(2).cells.size(), 3);
//        Assert.assertEquals((String) project.rows.get(2).getCellValue(0), "TooSho");
//        Assert.assertEquals((String) project.rows.get(2).getCellValue(1), "rt");
//        Assert.assertNull(project.rows.get(2).getCellValue(2));
    }

}
