/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.activemq.artemis.core.config.impl;

import java.util.EnumSet;

import org.apache.activemq.artemis.core.server.routing.KeyType;
import org.apache.activemq.artemis.core.server.ActiveMQMessageBundle;
import org.apache.activemq.artemis.core.server.ComponentConfigurationRoutingType;
import org.apache.activemq.artemis.core.server.JournalType;
import org.apache.activemq.artemis.api.core.RoutingType;
import org.apache.activemq.artemis.core.server.cluster.impl.MessageLoadBalancingType;
import org.apache.activemq.artemis.core.settings.impl.AddressFullMessagePolicy;
import org.apache.activemq.artemis.core.settings.impl.DeletionPolicy;
import org.apache.activemq.artemis.core.settings.impl.PageFullMessagePolicy;
import org.apache.activemq.artemis.core.settings.impl.SlowConsumerPolicy;
import org.apache.activemq.artemis.core.settings.impl.SlowConsumerThresholdMeasurementUnit;

/**
 * A Validators.
 */
public final class Validators {

   public interface Validator {

      void validate(String name, Object value);
   }

   public static final Validator NO_CHECK = new Validator() {
      @Override
      public void validate(final String name, final Object value) {
         return;
      }
   };

   public static final Validator NOT_NULL_OR_EMPTY = new Validator() {
      @Override
      public void validate(final String name, final Object value) {
         String str = (String) value;
         if (str == null || str.length() == 0) {
            throw ActiveMQMessageBundle.BUNDLE.emptyOrNull(name);
         }
      }
   };

   public static final Validator GT_ZERO = new Validator() {
      @Override
      public void validate(final String name, final Object value) {
         Number val = (Number) value;
         if (val.doubleValue() > 0) {
            // OK
         } else {
            throw ActiveMQMessageBundle.BUNDLE.greaterThanZero(name, val);
         }
      }
   };

   public static final Validator PERCENTAGE = new Validator() {
      @Override
      public void validate(final String name, final Object value) {
         Number val = (Number) value;
         if (val == null || (val.intValue() < 0 || val.intValue() > 100)) {
            throw ActiveMQMessageBundle.BUNDLE.notPercent(name, val);
         }
      }
   };

   public static final Validator PERCENTAGE_OR_MINUS_ONE = new Validator() {
      @Override
      public void validate(final String name, final Object value) {
         Number val = (Number) value;
         if (val == null || ((val.intValue() < 0 || val.intValue() > 100) && val.intValue() != -1)) {
            throw ActiveMQMessageBundle.BUNDLE.notPercentOrMinusOne(name, val);
         }
      }
   };

   public static final Validator GE_ZERO = new Validator() {
      @Override
      public void validate(final String name, final Object value) {
         Number val = (Number) value;
         if (val.doubleValue() >= 0) {
            // OK
         } else {
            throw ActiveMQMessageBundle.BUNDLE.greaterThanZero(name, val);
         }
      }
   };

   public static final Validator LE_ONE = new Validator() {
      @Override
      public void validate(final String name, final Object value) {
         Number val = (Number) value;
         if (val.doubleValue() <= 1) {
            // OK
         } else {
            throw ActiveMQMessageBundle.BUNDLE.lessThanOrEqualToOne(name, val);
         }
      }
   };

   public static final Validator MINUS_ONE_OR_GT_ZERO = new Validator() {
      @Override
      public void validate(final String name, final Object value) {
         Number val = (Number) value;
         if (val.doubleValue() == -1 || val.doubleValue() > 0) {
            // OK
         } else {
            throw ActiveMQMessageBundle.BUNDLE.greaterThanMinusOne(name, val);
         }
      }
   };

   public static final Validator MINUS_ONE_OR_GE_ZERO = new Validator() {
      @Override
      public void validate(final String name, final Object value) {
         Number val = (Number) value;
         if (val.doubleValue() == -1 || val.doubleValue() >= 0) {
            // OK
         } else {
            throw ActiveMQMessageBundle.BUNDLE.greaterThanZeroOrMinusOne(name, val);
         }
      }
   };

   public static final Validator POSITIVE_INT = new Validator() {
      @Override
      public void validate(final String name, final Object value) {
         Number val = (Number) value;
         if (val.longValue() > 0 && val.longValue() <= Integer.MAX_VALUE) {
            // OK
         } else {
            throw ActiveMQMessageBundle.BUNDLE.inRangeOfPositiveInt(name, val);
         }
      }
   };

   public static final Validator MINUS_ONE_OR_POSITIVE_INT = new Validator() {
      @Override
      public void validate(final String name, final Object value) {
         Number val = (Number) value;
         if (val.longValue() == -1 || (val.longValue() > 0 && val.longValue() <= Integer.MAX_VALUE)) {
            // OK
         } else {
            throw ActiveMQMessageBundle.BUNDLE.inRangeOfPositiveIntThanMinusOne(name, val);
         }
      }
   };

   public static final Validator THREAD_PRIORITY_RANGE = new Validator() {
      @Override
      public void validate(final String name, final Object value) {
         Number val = (Number) value;
         if (val.intValue() >= Thread.MIN_PRIORITY && val.intValue() <= Thread.MAX_PRIORITY) {
            // OK
         } else {
            throw ActiveMQMessageBundle.BUNDLE.mustbeBetween(name, Thread.MIN_PRIORITY, Thread.MAX_PRIORITY, value);
         }
      }
   };

   public static final Validator JOURNAL_TYPE = new Validator() {
      @Override
      public void validate(final String name, final Object value) {
         String val = (String) value;
         if (val == null || !EnumSet.allOf(JournalType.class).contains(JournalType.valueOf(val))) {
            throw ActiveMQMessageBundle.BUNDLE.invalidJournalType(val);
         }
      }
   };

   public static final Validator ADDRESS_FULL_MESSAGE_POLICY_TYPE = new Validator() {
      @Override
      public void validate(final String name, final Object value) {
         String val = (String) value;
         if (val == null || !val.equals(AddressFullMessagePolicy.PAGE.toString()) &&
            !val.equals(AddressFullMessagePolicy.DROP.toString()) &&
            !val.equals(AddressFullMessagePolicy.BLOCK.toString()) &&
            !val.equals(AddressFullMessagePolicy.FAIL.toString())) {
            throw ActiveMQMessageBundle.BUNDLE.invalidAddressFullPolicyType(val);
         }
      }
   };

   public static final Validator PAGE_FULL_MESSAGE_POLICY_TYPE = new Validator() {
      @Override
      public void validate(final String name, final Object value) {
         String val = (String) value;
         if (val == null ||
            !val.equals(PageFullMessagePolicy.DROP.toString()) &&
            !val.equals(PageFullMessagePolicy.FAIL.toString())) {
            throw ActiveMQMessageBundle.BUNDLE.invalidAddressFullPolicyType(val);
         }
      }
   };

   public static final Validator SLOW_CONSUMER_THRESHOLD_MEASUREMENT_UNIT = new Validator() {
      @Override
      public void validate(final String name, final Object value) {
         String val = (String) value;
         if (val == null || !val.equals(SlowConsumerThresholdMeasurementUnit.MESSAGES_PER_SECOND.toString()) &&
                            !val.equals(SlowConsumerThresholdMeasurementUnit.MESSAGES_PER_MINUTE.toString()) &&
                            !val.equals(SlowConsumerThresholdMeasurementUnit.MESSAGES_PER_HOUR.toString()) &&
                            !val.equals(SlowConsumerThresholdMeasurementUnit.MESSAGES_PER_DAY.toString())) {
            throw ActiveMQMessageBundle.BUNDLE.invalidSlowConsumerThresholdMeasurementUnit(val);
         }
      }
   };

   public static final Validator SLOW_CONSUMER_POLICY_TYPE = new Validator() {
      @Override
      public void validate(final String name, final Object value) {
         String val = (String) value;
         if (val == null || !val.equals(SlowConsumerPolicy.KILL.toString()) && !val.equals(SlowConsumerPolicy.NOTIFY.toString())) {
            throw ActiveMQMessageBundle.BUNDLE.invalidSlowConsumerPolicyType(val);
         }
      }
   };

   public static final Validator DELETION_POLICY_TYPE = new Validator() {
      @Override
      public void validate(final String name, final Object value) {
         String val = (String) value;
         if (val == null || !val.equals(DeletionPolicy.OFF.toString()) && !val.equals(DeletionPolicy.FORCE.toString())) {
            throw ActiveMQMessageBundle.BUNDLE.invalidDeletionPolicyType(val);
         }
      }
   };

   public static final Validator MESSAGE_LOAD_BALANCING_TYPE = new Validator() {
      @Override
      public void validate(final String name, final Object value) {
         String val = (String) value;
         if (val == null || !val.equals(MessageLoadBalancingType.OFF.toString()) &&
            !val.equals(MessageLoadBalancingType.OFF_WITH_REDISTRIBUTION.toString()) &&
            !val.equals(MessageLoadBalancingType.STRICT.toString()) &&
            !val.equals(MessageLoadBalancingType.ON_DEMAND.toString())) {
            throw ActiveMQMessageBundle.BUNDLE.invalidMessageLoadBalancingType(val);
         }
      }
   };

   public static final Validator ROUTING_TYPE = new Validator() {
      @Override
      public void validate(final String name, final Object value) {
         String val = (String) value;
         if (val == null || !val.equals(RoutingType.ANYCAST.toString()) &&
            !val.equals(RoutingType.MULTICAST.toString())) {
            throw ActiveMQMessageBundle.BUNDLE.invalidRoutingType(val);
         }
      }
   };

   public static final Validator COMPONENT_ROUTING_TYPE = new Validator() {
      @Override
      public void validate(final String name, final Object value) {
         String val = (String) value;
         if (val == null || !val.equals(ComponentConfigurationRoutingType.ANYCAST.toString()) &&
            !val.equals(ComponentConfigurationRoutingType.MULTICAST.toString()) &&
            !val.equals(ComponentConfigurationRoutingType.PASS.toString()) &&
            !val.equals(ComponentConfigurationRoutingType.STRIP.toString())) {
            throw ActiveMQMessageBundle.BUNDLE.invalidRoutingType(val);
         }
      }
   };

   public static final Validator MAX_QUEUE_CONSUMERS = new Validator() {
      @Override
      public void validate(String name, Object value) {
         int val = (Integer) value;
         if (val < -1) {
            throw ActiveMQMessageBundle.BUNDLE.invalidMaxConsumers(name, val);
         }
      }
   };

   public static final Validator KEY_TYPE = new Validator() {
      @Override
      public void validate(final String name, final Object value) {
         String val = (String) value;
         if (val == null || !EnumSet.allOf(KeyType.class).contains(KeyType.valueOf(val))) {
            throw ActiveMQMessageBundle.BUNDLE.invalidConnectionRouterKey(val);
         }
      }
   };

   public static final Validator NULL_OR_TWO_CHARACTERS = new Validator() {
      @Override
      public void validate(final String name, final Object value) {
         String val = (String) value;
         if (val != null && val.length() != 2) {
            throw ActiveMQMessageBundle.BUNDLE.wrongLength(name, val, val.length(), 2);
         }
      }
   };
}
