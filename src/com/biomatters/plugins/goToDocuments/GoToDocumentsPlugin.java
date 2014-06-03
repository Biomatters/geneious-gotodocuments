package com.biomatters.plugins.goToDocuments;

import com.biomatters.geneious.publicapi.plugin.DocumentOperation;
import com.biomatters.geneious.publicapi.plugin.GeneiousPlugin;

/**
 * Copyright (C) 2013-2014, Biomatters Ltd
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Foobar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */
public class GoToDocumentsPlugin extends GeneiousPlugin {

    public static final String PLUGIN_VERSION = "1.0.0";

    @Override
    public String getName() {
        return "Go To Documents";
    }

    @Override
    public String getDescription() {
        return "Provides a simple operation to select documents by their unique ID";
    }

    @Override
    public String getHelp() {
        return ""; //not shown anywhere
    }

    @Override
    public String getAuthors() {
        return "Biomatters Ltd.";
    }

    @Override
    public String getVersion() {
        return PLUGIN_VERSION;
    }

    @Override
    public String getMinimumApiVersion() {
        return "4.700";
    }

    @Override
    public int getMaximumApiVersion() {
        return 4;
    }

    @Override
    public DocumentOperation[] getDocumentOperations() {
        return new DocumentOperation[] {
                new GoToDocumentsOperation()
        };
    }
}
