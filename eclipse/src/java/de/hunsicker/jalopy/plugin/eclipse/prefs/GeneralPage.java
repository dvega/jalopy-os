/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 */
package de.hunsicker.jalopy.plugin.eclipse.prefs;

import de.hunsicker.jalopy.storage.ConventionDefaults;
import de.hunsicker.jalopy.storage.ConventionKeys;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;


/**
 * The page for setting code formatter options
 */
public class GeneralPage
    extends AbstractPage
{
    //~ Instance variables ---------------------------------------------------------------

    private Text _descriptionTextField;
    private Text _nameTextField;

    //~ Constructors ---------------------------------------------------------------------

    /**
     * Creates a new GeneralPage object.
     */
    public GeneralPage()
    {
    }

    //~ Methods --------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean performOk()
    {
        this.settings.put(ConventionKeys.CONVENTION_NAME, _nameTextField.getText());
        this.settings.put(
            ConventionKeys.CONVENTION_DESCRIPTION, _descriptionTextField.getText());

        return true;
    }


    /*
     * @see PreferencePage#createContents(Composite)
     */

    /**
     * DOCUMENT ME!
     *
     * @param parent DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected Control createContents(final Composite parent)
    {
        Composite container = new Composite(parent, SWT.NULL);
        GridLayout layout = new GridLayout();
        container.setLayout(layout);

        Group group = new Group(container, SWT.SHADOW_ETCHED_IN);
        layout = new GridLayout();
        layout.numColumns = 2;
        group.setLayout(layout);

        GridData gd =
            new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
        group.setLayoutData(gd);
        group.setText("Convention");

        Label nameLabel = new Label(group, SWT.NULL);
        nameLabel.setText("Name:");
        _nameTextField = new Text(group, SWT.SINGLE | SWT.BORDER);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        _nameTextField.setLayoutData(gd);
        _nameTextField.setText(
            this.settings.get(
                ConventionKeys.CONVENTION_NAME, ConventionDefaults.CONVENTION_NAME));

        Label descLabel = new Label(group, SWT.NULL);
        descLabel.setText("Description:");
        _descriptionTextField = new Text(group, SWT.SINGLE | SWT.BORDER);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        _descriptionTextField.setLayoutData(gd);
        _descriptionTextField.setText(
            this.settings.get(
                ConventionKeys.CONVENTION_DESCRIPTION,
                ConventionDefaults.CONVENTION_DESCRIPTION));

        Composite buttons = new Composite(container, SWT.NULL);
        layout = new GridLayout();
        buttons.setLayout(layout);
        layout.numColumns = 2;

        Button loadButton = new Button(buttons, SWT.PUSH | SWT.RIGHT);
        loadButton.setText("Load...");

        /*loadButton.addSelectionListener(new SelectionAdapter() {
           public void widgetSelected(SelectionEvent event)
           {
               FileDialog dialog = new FileDialog(parent.getShell(), SWT.OPEN);
               dialog.setFilterExtensions(new String[] { "*.jal" });
               String filename = dialog.open();
               if (filename != null)
               {
                   System.out.println(filename);
                   try
                   {
                       InputStream in = new BufferedInputStream(
                                                new FileInputStream(filename));
                       settings.importConvention(in);
                       _nameTextField.setText(settings.get(
                                                      Key.CONVENTION_NAME,
                                                      ConventionDefaults.CONVENTION_NAME));
                       _descriptionTextField.setText(prefs.get(
                                                             Key.CONVENTION_DESCRIPTION,
                                                             ConventionDefaults.CONVENTION_DESCRIPTION));
                   }
                   catch (Exception ex)
                   {
                       ex.printStackTrace();
                   }
               }
           }
           });*/
        Button saveButton = new Button(buttons, SWT.PUSH | SWT.RIGHT);
        saveButton.setText("Save...");

        /*saveButton.addSelectionListener(new SelectionAdapter() {
           public void widgetSelected(SelectionEvent event)
           {
               FileDialog dialog = new FileDialog(parent.getShell(), SWT.SAVE);
               dialog.setFilterExtensions(new String[] { "*.jal" });
               String filename = dialog.open();
               if (filename != null)
               {
                   File file = null;
                   if (filename.endsWith(".jal"))
                   {
                       file = new File(filename);
                   }
                   else
                   {
                       file = new File(filename + ".jal");
                   }
                   try
                   {
                       OutputStream out = new BufferedOutputStream(
                                                  new FileOutputStream(file));
                       performOk();
                       prefs.exportSubtree(out);
                   }
                   catch (Exception ex)
                   {
                       ex.printStackTrace();
                   }
               }
           }
           });*/
        return container;
    }


    /**
     * DOCUMENT ME!
     */
    protected void performConventionDefaults()
    {
    }
}
