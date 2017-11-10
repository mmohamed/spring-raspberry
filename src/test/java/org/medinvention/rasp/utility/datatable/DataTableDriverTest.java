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
package org.medinvention.rasp.utility.datatable;

import java.util.Arrays;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.medinvention.rasp.BaseTestUnit;
import org.medinvention.rasp.model.User;
import org.medinvention.rasp.utility.datatable.DataTableCallback;
import org.medinvention.rasp.utility.datatable.DataTableDriver;
import org.medinvention.rasp.utility.datatable.DataTableResponse;
import org.medinvention.rasp.utility.http.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.util.Assert;

public class DataTableDriverTest extends BaseTestUnit {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Autowired
    private DataTableDriver datatableDriver;

    @Test
    public void testBindException() throws Exception {
        DataTableDriver driver = new DataTableDriver();
        List<String> fields = Arrays.asList(new String[] { "id", "firstName", "lastName", "email", "level" });

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("X-Requested-With", "XMLHttpRequest");

        driver.bind(request, User.class, fields);

        exception.expect(Exception.class);
        exception.expectMessage("DataTableDriver must be cleared first !");

        driver.bind(new MockHttpServletRequest(), User.class, fields);
    }

    @Test
    public void testFilterException() throws Exception {
        DataTableDriver driver = new DataTableDriver();
        List<String> fields = Arrays.asList(new String[] { "id", "firstName", "lastName", "email", "level" });

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("bSearchable_0", "true");
        request.addParameter("sSearch_0", "1");
        request.addHeader("X-Requested-With", "XMLHttpRequest");

        driver.bind(request, User.class, fields);

        exception.expect(Exception.class);
        exception.expectMessage("Filters not implemented yet !");

        driver.getData();
    }

    @Test
    public void testSordException() throws Exception {
        DataTableDriver driver = new DataTableDriver();
        List<String> fields = Arrays.asList(new String[] { "id", "firstName", "lastName", "email", "level" });

        MockHttpServletRequest request = new MockHttpServletRequest();

        request.addParameter("iSortingCols", "1");
        request.setParameter("iSortCol_0", "0");
        request.addParameter("bSortable_0", "true");
        request.addParameter("sSortDir_0", "1");
        request.addHeader("X-Requested-With", "XMLHttpRequest");

        exception.expect(BadRequestException.class);
        exception.expectMessage("Invalid sord direction '1' for field 'id'");

        driver.bind(request, User.class, fields);
    }

    @Test
    public void testRequestTypeException() throws Exception {
        DataTableDriver driver = new DataTableDriver();
        List<String> fields = Arrays.asList(new String[] { "id", "firstName", "lastName", "email", "level" });

        exception.expect(BadRequestException.class);
        exception.expectMessage("Request must be ajax type !");

        driver.bind(new MockHttpServletRequest(), User.class, fields);
    }

    @Test
    public void testFieldsSizeException() throws Exception {
        DataTableDriver driver = new DataTableDriver();
        List<String> fields = Arrays.asList();

        exception.expect(Exception.class);
        exception.expectMessage("DataTable needs field !");

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("X-Requested-With", "XMLHttpRequest");

        driver.bind(request, User.class, fields);
    }

    @Test
    public void testGetDataException() throws Exception {
        DataTableDriver driver = new DataTableDriver();

        exception.expect(Exception.class);
        exception.expectMessage("DataTableDataSource must be binded first !");

        driver.getData();
    }

    @Test
    public void testSimpleSelection() throws Exception {
        List<String> fields = Arrays.asList(new String[] { "id", "firstName", "lastName", "email", "level" });

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("X-Requested-With", "XMLHttpRequest");

        this.datatableDriver.detach();
        this.datatableDriver.bind(request, User.class, fields);

        DataTableResponse response = this.datatableDriver.getData();

        Assert.isTrue(response.getAaData().size() == 3, "Has three user");
        Assert.isNull(response.getsEcho(), "Empty echo");
        Assert.isTrue(response.getiTotalDisplayRecords() == 3, "No filter enable ");
        Assert.isTrue(response.getiTotalRecords() == 3, "Just three user");
    }

    @Test
    public void testSimplePaginatedSelection() throws Exception {
        List<String> fields = Arrays.asList(new String[] { "id", "firstName", "lastName", "email", "level" });

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("iDisplayStart", "1");
        request.setParameter("iDisplayLength", "2");
        request.setParameter("sEcho", "test");
        request.addHeader("X-Requested-With", "XMLHttpRequest");

        this.datatableDriver.detach();
        this.datatableDriver.bind(request, User.class, fields);

        DataTableResponse response = this.datatableDriver.getData();

        Assert.isTrue(response.getAaData().size() == 1, "Has one user on last page");
        Assert.isTrue(response.getsEcho().compareTo("test") == 0, "Test echo");
        Assert.isTrue(response.getiTotalDisplayRecords() == 3, "No filter enable");
        Assert.isTrue(response.getiTotalRecords() == 3, "Just three user");

        request.setParameter("iDisplayStart", "2");

        this.datatableDriver.detach();
        this.datatableDriver.bind(request, User.class, fields);

        response = this.datatableDriver.getData();

        Assert.isTrue(response.getAaData().size() == 0, "empty page");
    }

    @Test
    public void testSimpleSortedSelection() throws Exception {
        List<String> fields = Arrays.asList(new String[] { "id", "firstName", "lastName", "email", "level" });

        MockHttpServletRequest request = new MockHttpServletRequest();

        request.setParameter("iSortingCols", "1");
        request.setParameter("iSortCol_0", "0");
        request.setParameter("bSortable_0", "true");
        request.setParameter("sSortDir_0", "desc");
        request.addHeader("X-Requested-With", "XMLHttpRequest");

        this.datatableDriver.detach();
        this.datatableDriver.bind(request, User.class, fields);

        DataTableResponse response = this.datatableDriver.getData();
        Assert.isTrue(response.getAaData().size() == 3, "Has three user");
        Assert.isTrue(response.getAaData().get(0).get(0).compareTo("3") == 0, "ID of last user");
    }

    @Test
    public void testSimpleFiltredSelection() throws Exception {
        List<String> fields = Arrays.asList(new String[] { "id", "firstName", "lastName", "email", "level" });

        MockHttpServletRequest request = new MockHttpServletRequest();

        request.setParameter("sSearch", "admin");
        request.addHeader("X-Requested-With", "XMLHttpRequest");

        this.datatableDriver.detach();
        this.datatableDriver.bind(request, User.class, fields);

        DataTableResponse response = this.datatableDriver.getData(new DataTableCallback() {
            @Override
            public void callback(DataTableResponse response) {
                for (int i = 0; i < response.getAaData().size(); i++) {

                    String level = response.getAaData().get(i).get(4);

                    if (level != null) {
                        response.getAaData().get(i).set(4,
                                level.substring(0, 1).toUpperCase() + level.substring(1).toLowerCase());
                    }
                }
            }
        });

        Assert.isTrue(response.getAaData().size() == 1, "Has one admin user");
        Assert.isTrue(response.getAaData().get(0).get(3).compareTo("admin@medinvention.ext.io") == 0,
                "Validate admin mail");
        Assert.isTrue(response.getAaData().get(0).get(4).compareTo("Actif") == 0, "Validate callback");
    }
}
