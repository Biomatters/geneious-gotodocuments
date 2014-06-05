package com.biomatters.plugins.goToDocuments;

import com.biomatters.geneious.publicapi.components.Dialogs;
import com.biomatters.geneious.publicapi.documents.*;
import com.biomatters.geneious.publicapi.plugin.*;
import com.biomatters.geneious.publicapi.utilities.StandardIcons;
import com.biomatters.geneious.publicapi.utilities.StringUtilities;
import jebl.util.ProgressListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) 2013-2014, Biomatters Ltd
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
public class GoToDocumentsOperation extends DocumentOperation {

    static final String URNS_OPTION_NAME = "urns";

    @Override
    public GeneiousActionOptions getActionOptions() {
        return new GeneiousActionOptions("Go to Documents", "Select documents by entering their unique IDs.", StandardIcons.document.getIcons())
                .setMainMenuLocation(GeneiousActionOptions.MainMenu.Edit).setAvailableToWorkflows(false);
    }

    @Override
    public String getHelp() {
        return "Enter a list of unique IDs (URN) to select the documents with those IDs. By default displays " +
                "the IDs of the currently selected documents.";
    }

    @Override
    public DocumentSelectionSignature[] getSelectionSignatures() {
        return new DocumentSelectionSignature[] {
                new DocumentSelectionSignature(PluginDocument.class, 0, Integer.MAX_VALUE)
        };
    }

    @Override
    public Options getOptions(DocumentOperationInput operationInput) throws DocumentOperationException {
        Options options = new Options(GoToDocumentsOperation.class);
        options.addLabel("Unique IDs (URNs), one per line:", false, true);
        String defaultString = "";
        for (AnnotatedPluginDocument annotatedPluginDocument : operationInput.getInputDocumentsArray()) {
            defaultString += annotatedPluginDocument.getURN() + "\n";
        }
        options.addMultipleLineStringOption(URNS_OPTION_NAME, "", defaultString, 5, false).setValue(defaultString);
        return options;
    }

    @Override
    public void performOperation(AnnotatedPluginDocument[] annotatedDocuments, ProgressListener progressListener, Options options, SequenceSelection sequenceSelection, OperationCallback callback) throws DocumentOperationException {
        try {
            String[] urnStrings = options.getValueAsString(URNS_OPTION_NAME).split("\n");
            List<URN> validUrns = new ArrayList<URN>();
            List<String> brokenUrns = new ArrayList<String>();
            List<URN> missingUrns = new ArrayList<URN>();
            for (String urnString : urnStrings) {
                try {
                    String trimmedString = urnString.trim();
                    if (trimmedString.isEmpty()) {
                        continue;
                    }
                    URN urn = new URN(trimmedString);
                    if (DocumentUtilities.getDocumentByURN(urn) == null) {
                        missingUrns.add(urn);
                        continue;
                    }
                    validUrns.add(urn);
                } catch (MalformedURNException e) {
                    brokenUrns.add(urnString);
                }
            }
            if (!brokenUrns.isEmpty()) {
                if (!Dialogs.showContinueCancelDialog("Some of the URNs you entered are invalid. They will be ignored:\n\n" + StringUtilities.join("\n", brokenUrns),
                        "Invalid URNs", null, Dialogs.DialogIcon.INFORMATION)) {
                    return;
                }
            }
            if (!missingUrns.isEmpty()) {
                if (!Dialogs.showContinueCancelDialog("Some of the URNs you entered cannot be found. They will be ignored:\n\n" + StringUtilities.join("\n", missingUrns),
                        "Missing URNs", null, Dialogs.DialogIcon.INFORMATION)) {
                    return;
                }
            }
            DocumentUtilities.selectDocuments(validUrns);
        } finally {
            progressListener.setProgress(1.0);
        }
    }
}
