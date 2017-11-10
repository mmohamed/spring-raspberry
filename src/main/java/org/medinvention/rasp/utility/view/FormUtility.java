/*******************************************************************************
 * Copyright 2017 Marouan MOHAMED
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package org.medinvention.rasp.utility.view;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

public class FormUtility {

    public static List<String> bind(HttpServletRequest request, Object viewObject, Object daoObject) throws Exception {

        if (viewObject.getClass() != daoObject.getClass()) {
            throw new Exception("View object and dao object must have same type (class) !");
        }

        List<String> errorsField = new ArrayList<String>();

        // set field value
        for (Entry<String, String[]> parameter : request.getParameterMap().entrySet()) {

            // build setter/getter method
            String setMethodName = "set" + parameter.getKey().substring(0, 1).toUpperCase()
                    + parameter.getKey().substring(1);
            String getMethodName = "get" + parameter.getKey().substring(0, 1).toUpperCase()
                    + parameter.getKey().substring(1);

            try {
                Method getMethod = daoObject.getClass().getMethod(getMethodName);
                Method setMethod = daoObject.getClass().getMethod(setMethodName, getMethod.getReturnType());
                setMethod.invoke(daoObject, getMethod.invoke(viewObject));
            }
            catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException exception) {
                errorsField.add(parameter.getKey());
            }
        }

        return errorsField;
    }
}
