/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.component.smpp;

import java.util.Date;
import java.util.TimeZone;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultExchange;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.bean.SMSCDeliveryReceipt;
import org.jsmpp.bean.TypeOfNumber;
import org.jsmpp.session.SMPPSession;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.easymock.EasyMock.aryEq;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.isNull;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;

public class SmppReplaceSmCommandTest {
    
    private static TimeZone defaultTimeZone;

    private SMPPSession session;
    private SmppConfiguration config;
    private SmppReplaceSmCommand command;
    
    @BeforeClass
    public static void setUpBeforeClass() {
        defaultTimeZone = TimeZone.getDefault();
        
        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
    }
    
    @AfterClass
    public static void tearDownAfterClass() {
        if (defaultTimeZone != null) {
            TimeZone.setDefault(defaultTimeZone);            
        }
    }
    
    @Before
    public void setUp() {
        session = createMock(SMPPSession.class);
        config = new SmppConfiguration();
        
        command = new SmppReplaceSmCommand(session, config);
    }
    
    @Test
    public void executeWithConfigurationData() throws Exception {
        Exchange exchange = new DefaultExchange(new DefaultCamelContext(), ExchangePattern.InOut);
        exchange.getIn().setHeader(SmppConstants.COMMAND, "ReplaceSm");
        exchange.getIn().setHeader(SmppConstants.ID, "1");
        exchange.getIn().setBody("new short message body");
        session.replaceShortMessage(eq("1"), eq(TypeOfNumber.UNKNOWN), eq(NumberingPlanIndicator.UNKNOWN), eq("1616"), (String) isNull(), (String) isNull(),
                eq(new RegisteredDelivery(SMSCDeliveryReceipt.SUCCESS_FAILURE)), eq((byte) 0), aryEq("new short message body".getBytes()));
        
        replay(session);
        
        command.execute(exchange);
        
        verify(session);
        
        assertEquals("1", exchange.getOut().getHeader(SmppConstants.ID));
    }
    
    @Test
    public void execute() throws Exception {
        Exchange exchange = new DefaultExchange(new DefaultCamelContext(), ExchangePattern.InOut);
        exchange.getIn().setHeader(SmppConstants.COMMAND, "ReplaceSm");
        exchange.getIn().setHeader(SmppConstants.ID, "1");
        exchange.getIn().setHeader(SmppConstants.SOURCE_ADDR_TON, TypeOfNumber.NATIONAL.value());
        exchange.getIn().setHeader(SmppConstants.SOURCE_ADDR_NPI, NumberingPlanIndicator.NATIONAL.value());
        exchange.getIn().setHeader(SmppConstants.SOURCE_ADDR, "1818");
        exchange.getIn().setHeader(SmppConstants.SCHEDULE_DELIVERY_TIME, new Date(1111111));
        exchange.getIn().setHeader(SmppConstants.VALIDITY_PERIOD, new Date(2222222));
        exchange.getIn().setHeader(SmppConstants.REGISTERED_DELIVERY, new RegisteredDelivery(SMSCDeliveryReceipt.SUCCESS).value());
        exchange.getIn().setBody("new short message body");
        session.replaceShortMessage(eq("1"), eq(TypeOfNumber.NATIONAL), eq(NumberingPlanIndicator.NATIONAL), eq("1818"), eq("-300101001831100-"), eq("-300101003702200-"),
                eq(new RegisteredDelivery(SMSCDeliveryReceipt.SUCCESS)), eq((byte) 0), aryEq("new short message body".getBytes()));
        
        replay(session);
        
        command.execute(exchange);
        
        verify(session);
        
        assertEquals("1", exchange.getOut().getHeader(SmppConstants.ID));
    }
    
    @Test
    public void executeWithValidityPeriodAsString() throws Exception {
        Exchange exchange = new DefaultExchange(new DefaultCamelContext(), ExchangePattern.InOut);
        exchange.getIn().setHeader(SmppConstants.COMMAND, "ReplaceSm");
        exchange.getIn().setHeader(SmppConstants.ID, "1");
        exchange.getIn().setHeader(SmppConstants.SOURCE_ADDR_TON, TypeOfNumber.NATIONAL.value());
        exchange.getIn().setHeader(SmppConstants.SOURCE_ADDR_NPI, NumberingPlanIndicator.NATIONAL.value());
        exchange.getIn().setHeader(SmppConstants.SOURCE_ADDR, "1818");
        exchange.getIn().setHeader(SmppConstants.SCHEDULE_DELIVERY_TIME, new Date(1111111));
        exchange.getIn().setHeader(SmppConstants.VALIDITY_PERIOD, "000003000000000R"); // three days
        exchange.getIn().setHeader(SmppConstants.REGISTERED_DELIVERY, new RegisteredDelivery(SMSCDeliveryReceipt.SUCCESS).value());
        exchange.getIn().setBody("new short message body");
        session.replaceShortMessage(eq("1"), eq(TypeOfNumber.NATIONAL), eq(NumberingPlanIndicator.NATIONAL), eq("1818"), eq("-300101001831100-"), eq("000003000000000R"),
                eq(new RegisteredDelivery(SMSCDeliveryReceipt.SUCCESS)), eq((byte) 0), aryEq("new short message body".getBytes()));
        
        replay(session);
        
        command.execute(exchange);
        
        verify(session);
        
        assertEquals("1", exchange.getOut().getHeader(SmppConstants.ID));
    }
}