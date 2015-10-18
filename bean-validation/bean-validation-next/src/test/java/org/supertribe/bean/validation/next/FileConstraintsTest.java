/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.supertribe.bean.validation.next;

import org.junit.Assert;
import org.junit.Test;

import javax.enterprise.util.AnnotationLiteral;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileConstraintsTest extends Assert {

    @Test
    public void executable() throws Exception {
        assertConstraints(new Object() {

            @Executable
            final File executable = Files.createTempFile("test", ".tmp", perms("--x--x--x")).toFile();

            @Executable
            final File notExecutable = Files.createTempFile("test", ".tmp", perms("rw-rw-rw-")).toFile();

        }, "Not executable");
    }

    @Test
    public void writable() throws Exception {
        assertConstraints(new Object() {

            @Writable
            final File writable = Files.createTempFile("test", ".tmp", perms("-w--w--w-")).toFile();

            @Writable
            final File notWritable = Files.createTempFile("test", ".tmp", perms("r-xr-xr-x")).toFile();

        }, "Not writable");
    }

    @Test
    public void readable() throws Exception {
        assertConstraints(new Object() {

            @Readable
            final File readable = Files.createTempFile("test", ".tmp", perms("r--r--r--")).toFile();

            @Readable
            final File notReadable = Files.createTempFile("test", ".tmp", perms("-wx-wx-wx")).toFile();

        }, "Not readable");
    }

    @Test
    public void directory() throws Exception {
        assertConstraints(new Object() {

            @Directory
            final File directory = Files.createTempDirectory("test", perms("rw-rw-rw-")).toFile();

            @Directory
            final File notDirectory = Files.createTempFile("test", ".tmp", perms("rw-rw-rw-")).toFile();

        }, "Not a directory");
    }

    @Test
    public void exists() throws Exception {
        assertConstraints(new Object() {

            @Exists
            final File exists = Files.createTempFile("test", ".tmp", perms("--x--x--x")).toFile();

            @Exists
            final File nonExistent = Files.createTempFile("test", ".tmp", perms("rw-rw-rw-")).toFile();

            {
                assertTrue(nonExistent.delete());
            }

        }, "Does not exist");


    }

    private void assertConstraints(final Object object, final String... violations) {
        final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        final Validator validator = factory.getValidator();

        final Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object);

        final List<String> actual = constraintViolations.stream()
                .map(ConstraintViolation::getMessage)
                .sorted()
                .collect(Collectors.toList());

        final List<String> expected = Stream.of(violations)
                .sorted()
                .collect(Collectors.toList());

        assertEquals(expected, actual);
    }

    private static FileAttribute<Set<PosixFilePermission>> perms(final String perms) {
        return PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString(perms));
    }

}
