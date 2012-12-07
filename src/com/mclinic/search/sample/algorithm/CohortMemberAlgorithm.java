/**
 * Copyright 2012 Muzima Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mclinic.search.sample.algorithm;

import java.text.ParseException;

import android.util.Log;
import com.burkeware.search.api.serialization.Algorithm;
import com.burkeware.search.api.util.ISO8601Util;
import com.jayway.jsonpath.JsonPath;
import com.mclinic.search.sample.domain.Patient;

public class CohortMemberAlgorithm implements Algorithm {

    /**
     * Implementation of this method will define how the patient will be serialized from the JSON representation.
     *
     * @param serialized the json representation
     * @return the concrete patient object
     */
    @Override
    public Patient deserialize(final String serialized) {
        Patient patient = new Patient();

        // get the full json object representation and then pass this around to the next JsonPath.read()
        // this should minimize the time for the subsequent read() call
        Object jsonObject = JsonPath.read(serialized, "$");

        String uuid = JsonPath.read(jsonObject, "$.patient.uuid");
        patient.setUuid(uuid);

        String name = JsonPath.read(jsonObject, "$.patient.person.display");
        patient.setName(name);

        String identifier = JsonPath.read(jsonObject, "$.patient.identifiers[0].display");
        patient.setIdentifier(identifier);

        String gender = JsonPath.read(jsonObject, "$.patient.person.gender");
        patient.setGender(gender);

        String birthdate = JsonPath.read(jsonObject, "$.patient.person.birthdate");
        try {
            patient.setBirthdate(ISO8601Util.toCalendar(birthdate).getTime());
        } catch (ParseException e) {
            Log.i(this.getClass().getSimpleName(), "Unable to parse date data from json payload.");
        }

        patient.setJson(serialized);

        return patient;
    }

    /**
     * Implementation of this method will define how the patient will be deserialized into the JSON representation.
     *
     * @param object the patient
     * @return the json representation
     */
    @Override
    public String serialize(final Object object) {
        Patient patient = (Patient) object;
        return patient.getJson();
    }
}
