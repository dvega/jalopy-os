/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. Neither the name of the Jalopy project nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
 * TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * $Id$
 */
package de.hunsicker.jalopy.ui;

import de.hunsicker.jalopy.prefs.Defaults;
import de.hunsicker.jalopy.prefs.Keys;
import de.hunsicker.jalopy.prefs.Preferences;
import de.hunsicker.ui.util.SwingHelper;
import de.hunsicker.util.StringHelper;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.PatternMatcher;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;


/**
 * A component that can be used to display/edit the Jalopy Javadoc comment
 * preferences.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public class JavadocPanel
    extends AbstractPreferencesPanel
{
    //~ Static variables/initializers ·········································

    private static final int ROW_CLASS = 0;
    private static final int ROW_CTOR = 1;
    private static final int ROW_METHOD = 2;
    private static final int ROW_VARIABLE = 3;
    private static final int COL_PUBLIC = 1;
    private static final int COL_PROTECTED = 2;
    private static final int COL_DEFAULT = 3;
    private static final int COL_PRIVATE = 4;
    private static final String TPL_CLASS = "Class";
    private static final String TPL_INTERFACE = "Interface";
    private static final String TPL_CTOR = "Constructor";
    private static final String TPL_METHOD = "Method";
    private static final String TPL_FIELD = "Field";
    private static final String DELIMETER = "|";

    //~ Instance variables ····················································

    private AddRemoveList _inlineTagsList;
    private AddRemoveList _standardTagsList;
    private DataModel _tableModel;
    private JCheckBox _checkCheckBox;
    private JCheckBox _createInnerCheckBox;
    private JCheckBox _parseCheckBox;
    private JCheckBox _singleLineFieldCommentsCheckBox;
    private final PatternMatcher _matcher = new Perl5Matcher();
    private Pattern _bottomTextPattern;
    private Pattern _exceptionPattern;
    private Pattern _paramPattern;
    private Pattern _returnPattern;
    private Pattern _tagNamePattern;
    private Pattern _templatePattern;
    private Pattern _topTextPattern;
    private TemplateContainer _templatesContainer;
    private boolean _disposed;
    {
        PatternCompiler compiler = new Perl5Compiler();

        try
        {
            _tagNamePattern = compiler.compile("@[a-zA-Z]+");
            _topTextPattern = compiler.compile("\\/\\*\\*(?:.*)+\\n\\s*\\*\\s*(.*)(?:\\n)*");
            _paramPattern = compiler.compile("\\s*\\*\\s*@param\\s+\\$paramType\\$\\s+(?:.+)");
            _returnPattern = compiler.compile("\\s*\\*\\s*@return\\s+(?:.+)");
            _exceptionPattern = compiler.compile("\\s*\\*\\s*@(?:throws|exception)\\s+\\$exceptionType\\$\\s+(?:.+)");
            _bottomTextPattern = compiler.compile("\\s*(?:\\*)+/");
            _templatePattern = compiler.compile("\\/\\*\\*[^*]*\\*+([^//*][^*]*\\*+)*\\/");
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    //~ Constructors ··························································

    /**
     * Creates a new JavadocPanel object.
     */
    public JavadocPanel()
    {
        initialize();
    }


    /**
     * Creates a new JavadocPanel.
     *
     * @param container the parent container.
     */
    JavadocPanel(PreferencesContainer container)
    {
        super(container);
        initialize();
    }

    //~ Methods ·······························································

    /**
     * Frees allocated resources.
     */
    public void dispose()
    {
        if (!_disposed)
        {
            _inlineTagsList = null;
            _standardTagsList = null;
            _tableModel = null;
            _checkCheckBox = null;
            _createInnerCheckBox = null;
            _parseCheckBox = null;
            _templatesContainer.dispose();
            _disposed = true;
        }
    }


    /**
     * {@inheritDoc}
     */
    public void store()
    {
        int classMask = 0;

        if (((Boolean)_tableModel.data[ROW_CLASS][COL_PUBLIC]).booleanValue())
        {
            classMask += Modifier.PUBLIC;
        }

        if (((Boolean)_tableModel.data[ROW_CLASS][COL_PROTECTED]).booleanValue())
        {
            classMask += Modifier.PROTECTED;
        }

        if (((Boolean)_tableModel.data[ROW_CLASS][COL_DEFAULT]).booleanValue())
        {
            classMask += Modifier.FINAL;
        }

        if (((Boolean)_tableModel.data[ROW_CLASS][COL_PRIVATE]).booleanValue())
        {
            classMask += Modifier.PRIVATE;
        }

        int ctorMask = 0;

        if (((Boolean)_tableModel.data[ROW_CTOR][COL_PUBLIC]).booleanValue())
        {
            ctorMask += Modifier.PUBLIC;
        }

        if (((Boolean)_tableModel.data[ROW_CTOR][COL_PROTECTED]).booleanValue())
        {
            ctorMask += Modifier.PROTECTED;
        }

        if (((Boolean)_tableModel.data[ROW_CTOR][COL_DEFAULT]).booleanValue())
        {
            ctorMask += Modifier.FINAL;
        }

        if (((Boolean)_tableModel.data[ROW_CTOR][COL_PRIVATE]).booleanValue())
        {
            ctorMask += Modifier.PRIVATE;
        }

        int methodMask = 0;

        if (((Boolean)_tableModel.data[ROW_METHOD][COL_PUBLIC]).booleanValue())
        {
            methodMask += Modifier.PUBLIC;
        }

        if (((Boolean)_tableModel.data[ROW_METHOD][COL_PROTECTED]).booleanValue())
        {
            methodMask += Modifier.PROTECTED;
        }

        if (((Boolean)_tableModel.data[ROW_METHOD][COL_DEFAULT]).booleanValue())
        {
            methodMask += Modifier.FINAL;
        }

        if (((Boolean)_tableModel.data[ROW_METHOD][COL_PRIVATE]).booleanValue())
        {
            methodMask += Modifier.PRIVATE;
        }

        int variableMask = 0;

        if (((Boolean)_tableModel.data[ROW_VARIABLE][COL_PUBLIC]).booleanValue())
        {
            variableMask += Modifier.PUBLIC;
        }

        if (((Boolean)_tableModel.data[ROW_VARIABLE][COL_PROTECTED]).booleanValue())
        {
            variableMask += Modifier.PROTECTED;
        }

        if (((Boolean)_tableModel.data[ROW_VARIABLE][COL_DEFAULT]).booleanValue())
        {
            variableMask += Modifier.FINAL;
        }

        if (((Boolean)_tableModel.data[ROW_VARIABLE][COL_PRIVATE]).booleanValue())
        {
            variableMask += Modifier.PRIVATE;
        }

        this.prefs.putInt(Keys.COMMENT_JAVADOC_CTOR_MASK, ctorMask);
        this.prefs.putInt(Keys.COMMENT_JAVADOC_METHOD_MASK, methodMask);
        this.prefs.putInt(Keys.COMMENT_JAVADOC_CLASS_MASK, classMask);
        this.prefs.putInt(Keys.COMMENT_JAVADOC_VARIABLE_MASK, variableMask);
        this.prefs.putBoolean(Keys.COMMENT_JAVADOC_FIELDS_SHORT,
                              _singleLineFieldCommentsCheckBox.isSelected());

        this.prefs.putBoolean(Keys.COMMENT_JAVADOC_PARSE,
                              _parseCheckBox.isSelected());
        this.prefs.putBoolean(Keys.COMMENT_JAVADOC_CHECK_TAG,
                              _checkCheckBox.isSelected());
        this.prefs.putBoolean(Keys.COMMENT_JAVADOC_INNER_CLASS,
                              _createInnerCheckBox.isSelected());
        this.prefs.put(Keys.COMMENT_JAVADOC_TAGS_STANDARD,
                       encodeTags(_standardTagsList.getValues()));
        this.prefs.put(Keys.COMMENT_JAVADOC_TAGS_INLINE,
                       encodeTags(_inlineTagsList.getValues()));
        _templatesContainer.store();
    }


    /**
     * {@inheritDoc}
     */
    public void validateSettings()
        throws ValidationException
    {
        //_templatesContainer.validateSettings();
    }


    private JPanel createGeneralPane()
    {
        _parseCheckBox = new JCheckBox("Parse/format comments",
                                       this.prefs.getBoolean(
                                                             Keys.COMMENT_JAVADOC_PARSE,
                                                             Defaults.COMMENT_JAVADOC_PARSE));
        _parseCheckBox.addActionListener(this.trigger);
        _checkCheckBox = new JCheckBox("Correct tags",
                                       this.prefs.getBoolean(
                                                             Keys.COMMENT_JAVADOC_CHECK_TAG,
                                                             Defaults.COMMENT_JAVADOC_CHECK_TAG));
        _checkCheckBox.addActionListener(this.trigger);
        _parseCheckBox.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent ev)
                {
                    if (_parseCheckBox.isSelected())
                    {
                        _checkCheckBox.setEnabled(true);
                        _singleLineFieldCommentsCheckBox.setEnabled(true);
                    }
                    else
                    {
                        _checkCheckBox.setEnabled(false);
                        _singleLineFieldCommentsCheckBox.setEnabled(false);
                    }
                }
            });

        JPanel generalPanel = new JPanel();
        generalPanel.setLayout(new BoxLayout(generalPanel, BoxLayout.Y_AXIS));
        generalPanel.setBorder(BorderFactory.createCompoundBorder(
                                                                  BorderFactory.createTitledBorder("General"),
                                                                  BorderFactory.createEmptyBorder(0, 5, 5, 5)));
        generalPanel.add(_parseCheckBox);
        generalPanel.add(_checkCheckBox);
        _tableModel = new DataModel();

        JTable table = new JTable(_tableModel);
        table.getTableHeader().setResizingAllowed(false);
        table.getTableHeader().setReorderingAllowed(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setCellSelectionEnabled(false);
        table.setColumnSelectionAllowed(false);
        table.setRowSelectionAllowed(false);

        //initializeColumnSizes(table);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport()
                  .setBackground((Color)UIManager.get("Table.background"));

        int height = SwingHelper.getTableHeight(table);
        scrollPane.setPreferredSize(new Dimension(300, height + 17));

        JPanel createPanel = new JPanel();
        GridBagLayout createLayout = new GridBagLayout();
        createPanel.setBorder(BorderFactory.createCompoundBorder(
                                                                 BorderFactory.createTitledBorder("Generation"),
                                                                 BorderFactory.createEmptyBorder(0, 5, 5, 5)));
        createPanel.setLayout(createLayout);

        GridBagConstraints c = new GridBagConstraints();
        c.insets.top = 5;
        SwingHelper.setConstraints(c, 0, 0, 8, 8, 1.0, 1.0,
                                   GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.BOTH, c.insets, 0, 0);
        createLayout.setConstraints(scrollPane, c);
        createPanel.add(scrollPane);
        c.insets.bottom = 0;
        _createInnerCheckBox = new JCheckBox("Include inner classes",
                                             this.prefs.getBoolean(
                                                                   Keys.COMMENT_JAVADOC_INNER_CLASS,
                                                                   Defaults.COMMENT_JAVADOC_INNER_CLASS));
        _createInnerCheckBox.addActionListener(this.trigger);
        SwingHelper.setConstraints(c, 0, 9, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        createLayout.setConstraints(_createInnerCheckBox, c);
        createPanel.add(_createInnerCheckBox);

        JPanel miscPanel = new JPanel();
        miscPanel.setLayout(new BoxLayout(miscPanel, BoxLayout.Y_AXIS));
        miscPanel.setBorder(BorderFactory.createCompoundBorder(
                                                               BorderFactory.createTitledBorder("Misc"),
                                                               BorderFactory.createEmptyBorder(0, 5, 5, 5)));
        _singleLineFieldCommentsCheckBox = new JCheckBox("Field comments in single line",
                                                         this.prefs.getBoolean(
                                                                               Keys.COMMENT_JAVADOC_FIELDS_SHORT,
                                                                               Defaults.COMMENT_JAVADOC_FIELDS_SHORT));
        _singleLineFieldCommentsCheckBox.addActionListener(this.trigger);
        miscPanel.add(_singleLineFieldCommentsCheckBox);

        if (_parseCheckBox.isSelected())
        {
            _checkCheckBox.setEnabled(true);
            _singleLineFieldCommentsCheckBox.setEnabled(true);
        }
        else
        {
            _checkCheckBox.setEnabled(false);
            _singleLineFieldCommentsCheckBox.setEnabled(false);
        }

        GridBagLayout layout = new GridBagLayout();
        JPanel panel = new JPanel();
        panel.setLayout(layout);
        c.insets.top = 10;
        c.insets.left = 5;
        c.insets.right = 5;
        SwingHelper.setConstraints(c, 0, 0, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        layout.setConstraints(generalPanel, c);
        panel.add(generalPanel);

        SwingHelper.setConstraints(c, 0, 1, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        layout.setConstraints(createPanel, c);
        panel.add(createPanel);

        c.insets.bottom = 10;
        SwingHelper.setConstraints(c, 0, 2, GridBagConstraints.REMAINDER, 1,
                                   1.0, 1.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        layout.setConstraints(miscPanel, c);
        panel.add(miscPanel);

        _tableModel.addTableModelListener(new TableModelListener()
            {
                public void tableChanged(TableModelEvent ev)
                {
                    trigger.actionPerformed(null);
                }
            });

        return panel;
    }


    /**
     * Returns the custom tags pane.
     *
     * @return the custom tags pane.
     */
    private JPanel createTagsPane()
    {
        GridBagLayout tagsPanelLayout = new GridBagLayout();
        JPanel tagsPanel = new JPanel();
        tagsPanel.setLayout(tagsPanelLayout);

        JPanel standardTagsPanel = new JPanel();
        GridBagConstraints c = new GridBagConstraints();
        GridBagLayout standardPanelLayout = new GridBagLayout();
        standardTagsPanel.setLayout(standardPanelLayout);
        standardTagsPanel.setBorder(BorderFactory.createCompoundBorder(
                                                                       BorderFactory.createTitledBorder("Standard Tags"),
                                                                       BorderFactory.createEmptyBorder(0, 5, 5, 5)));

        String standardTagsString = this.prefs.get(Keys.COMMENT_JAVADOC_TAGS_STANDARD,
                                                   Defaults.COMMENT_JAVADOC_TAGS_STANDARD);
        Collection standardTags = decodeTags(standardTagsString);
        _standardTagsList = new AddRemoveList("Add new standard tag",
                                              "Enter tag name:", standardTags);

        ListDataHandler dataHandler = new ListDataHandler();
        _standardTagsList.getModel().addListDataListener(dataHandler);

        JScrollPane standardTagsScrollPane = new JScrollPane(_standardTagsList);
        c.insets.top = 0;
        c.insets.bottom = 0;
        c.insets.left = 0;
        c.insets.right = 0;
        SwingHelper.setConstraints(c, 0, 0, 10, 5, 1.0, 1.0,
                                   GridBagConstraints.NORTH,
                                   GridBagConstraints.BOTH, c.insets, 0, 0);
        standardPanelLayout.setConstraints(standardTagsScrollPane, c);
        standardTagsPanel.add(standardTagsScrollPane);

        JButton standardTagsAddButton = _standardTagsList.getAddButton();
        c.insets.left = 5;
        c.insets.bottom = 2;
        SwingHelper.setConstraints(c, 11, 0, GridBagConstraints.REMAINDER, 1,
                                   0.0, 0.0, GridBagConstraints.WEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        standardPanelLayout.setConstraints(standardTagsAddButton, c);
        standardTagsPanel.add(standardTagsAddButton);

        JButton standardTagsRemoveButton = _standardTagsList.getRemoveButton();
        SwingHelper.setConstraints(c, 11, 1, GridBagConstraints.REMAINDER, 1,
                                   0.0, 0.0, GridBagConstraints.WEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        standardPanelLayout.setConstraints(standardTagsRemoveButton, c);
        standardTagsPanel.add(standardTagsRemoveButton);

        JPanel inlineTagsPanel = new JPanel();
        GridBagLayout inlinePanelLayout = new GridBagLayout();
        inlineTagsPanel.setLayout(inlinePanelLayout);
        inlineTagsPanel.setBorder(BorderFactory.createCompoundBorder(
                                                                     BorderFactory.createTitledBorder("In-line Tags"),
                                                                     BorderFactory.createEmptyBorder(0, 5, 5, 5)));

        String inlineTagsString = this.prefs.get(Keys.COMMENT_JAVADOC_TAGS_INLINE,
                                                 Defaults.COMMENT_JAVADOC_TAGS_INLINE);
        Collection inlineTags = decodeTags(inlineTagsString);
        _inlineTagsList = new AddRemoveList("Add new in-line tag",
                                            "Enter tag name:", inlineTags);
        _inlineTagsList.getModel().addListDataListener(dataHandler);
        c.insets.top = 0;
        c.insets.bottom = 0;
        c.insets.left = 0;
        c.insets.right = 0;

        JScrollPane inlineTagsScrollPane = new JScrollPane(_inlineTagsList);
        SwingHelper.setConstraints(c, 0, 0, 10, 5, 1.0, 1.0,
                                   GridBagConstraints.NORTH,
                                   GridBagConstraints.BOTH, c.insets, 0, 0);
        inlinePanelLayout.setConstraints(inlineTagsScrollPane, c);
        inlineTagsPanel.add(inlineTagsScrollPane);

        JButton inlineTagsAddButton = _inlineTagsList.getAddButton();
        c.insets.left = 5;
        c.insets.bottom = 2;
        SwingHelper.setConstraints(c, 11, 0, GridBagConstraints.REMAINDER, 1,
                                   0.0, 0.0, GridBagConstraints.WEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        inlinePanelLayout.setConstraints(inlineTagsAddButton, c);
        inlineTagsPanel.add(inlineTagsAddButton);

        JButton inlineTagsRemoveButton = _inlineTagsList.getRemoveButton();
        SwingHelper.setConstraints(c, 11, 1, GridBagConstraints.REMAINDER, 1,
                                   0.0, 0.0, GridBagConstraints.WEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        inlinePanelLayout.setConstraints(inlineTagsRemoveButton, c);
        inlineTagsPanel.add(inlineTagsRemoveButton);
        c.insets.top = 10;
        c.insets.bottom = 10;
        c.insets.left = 5;
        c.insets.right = 5;
        SwingHelper.setConstraints(c, 0, 1, GridBagConstraints.REMAINDER, 1,
                                   1.0, 1.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.BOTH, c.insets, 0, 0);
        tagsPanelLayout.setConstraints(standardTagsPanel, c);
        tagsPanel.add(standardTagsPanel);
        c.insets.top = 0;
        SwingHelper.setConstraints(c, 0, 2, GridBagConstraints.REMAINDER, 1,
                                   1.0, 1.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.BOTH, c.insets, 0, 0);
        tagsPanelLayout.setConstraints(inlineTagsPanel, c);
        tagsPanel.add(inlineTagsPanel);

        return tagsPanel;
    }


    /**
     * Creates a panel that allows the user to specify the different Javadoc
     * templates.
     *
     * @return the templates panel.
     *
     * @since 1.0b8
     */
    private JPanel createTemplatesPane()
    {
        final JPanel templatesPanel = new JPanel();
        templatesPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        GridBagLayout templatesLayout = new GridBagLayout();
        templatesPanel.setLayout(templatesLayout);

        GridBagConstraints c = new GridBagConstraints();
        String[] items =
        {
            TPL_CLASS, TPL_INTERFACE, TPL_CTOR, TPL_METHOD, TPL_FIELD
        };
        ComboBoxPanel chooseTemplateComboBoxPanel = new ComboBoxPanel("Show template for: ",
                                                                      items,
                                                                      TPL_CLASS);
        final JComboBox chooseTemplateComboBox = chooseTemplateComboBoxPanel.getComboBox();
        SwingHelper.setConstraints(c, 0, 0, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        templatesLayout.setConstraints(chooseTemplateComboBoxPanel, c);
        templatesPanel.add(chooseTemplateComboBoxPanel);
        c.insets.top = 5;
        _templatesContainer = new TemplateContainer(TPL_METHOD);
        SwingHelper.setConstraints(c, 0, 1, GridBagConstraints.REMAINDER, 1,
                                   1.0, 1.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.BOTH, c.insets, 0, 0);
        templatesLayout.setConstraints(_templatesContainer, c);
        templatesPanel.add(_templatesContainer);
        chooseTemplateComboBox.addItemListener(new ItemListener()
            {
                int index = 3; // index of the currently displayed item

                public void itemStateChanged(ItemEvent ev)
                {
                    switch (ev.getStateChange())
                    {
                        case ItemEvent.DESELECTED :

                            try
                            {
                                validateSettings();
                            }
                            catch (ValidationException ex)
                            {
                                // revert the selection
                                chooseTemplateComboBox.setSelectedIndex(this.index);

                                return;
                            }

                            String name = (String)chooseTemplateComboBox.getSelectedItem();

                            if ((name != null) && (name.length() > 0))
                            {
                                if (!name.equals(_templatesContainer.getCurrent()))
                                {
                                    _templatesContainer.switchPanels(name);
                                    invalidate();
                                    repaint();
                                }
                            }

                            index = chooseTemplateComboBox.getSelectedIndex();

                            break;
                    }
                }
            });
        chooseTemplateComboBox.setSelectedIndex(3);

        return templatesPanel;
    }


    /**
     * Decodes the given encoded tags string.
     *
     * @param tags encoded tags string.
     *
     * @return collection of the tags.
     *
     * @since 1.0b7
     */
    private Collection decodeTags(String tags)
    {
        List result = new ArrayList();

        for (StringTokenizer i = new StringTokenizer(tags, DELIMETER);
             i.hasMoreElements();)
        {
            result.add(i.nextToken());
        }

        return result;
    }


    /**
     * Encodes the given tag list as a string.
     *
     * @param tags tags to encode.
     *
     * @return encode (string delimeted) tag list.
     *
     * @since 1.0b7
     */
    private String encodeTags(Object[] tags)
    {
        StringBuffer buf = new StringBuffer();

        for (int i = 0; i < tags.length; i++)
        {
            buf.append(tags[i]);
            buf.append(DELIMETER);
        }

        if (buf.length() > 0)
        {
            buf.deleteCharAt(buf.length() - 1);
        }

        return buf.toString();
    }


    private void initialize()
    {
        JTabbedPane tabs = new JTabbedPane();
        tabs.add(createGeneralPane(), "General");
        tabs.add(createTemplatesPane(), "Templates");
        tabs.add(createTagsPane(), "Custom Tags");
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(tabs, BorderLayout.CENTER);
    }


    /**
     * This method picks good column sizes.
     *
     * @param table table to initialize
     */
    private void initializeColumnSizes(JTable table)
    {
        TableCellRenderer headerRenderer = table.getTableHeader()
                                                .getDefaultRenderer();

        for (int i = 1; i < 5; i++)
        {
            TableColumn column = table.getColumnModel().getColumn(i);
            Component comp = headerRenderer.getTableCellRendererComponent(null,
                                                                          column.getHeaderValue(),
                                                                          false,
                                                                          false, 0, 0);
            int headerWidth = comp.getPreferredSize().width;
            column.setPreferredWidth(headerWidth);
        }
    }

    //~ Inner Interfaces ······················································

    private static interface TemplatePanel
    {
        public void store();


        public void validateSettings()
            throws ValidationException;
    }

    //~ Inner Classes ·························································

    private static class DataModel
        extends AbstractTableModel
    {
        private static final Preferences _prefs = Preferences.getInstance();
        final String[] columnNames =
        {
            "                  ", "public", "protected", "default", "private"
        };
        final Object[][] data =
        {
            {
                "Classes/Interfaces",
                new Boolean(Modifier.isPublic(_prefs.getInt(
                                                            Keys.COMMENT_JAVADOC_CLASS_MASK,
                                                            Defaults.COMMENT_JAVADOC_CLASS_MASK))),
                new Boolean(Modifier.isProtected(_prefs.getInt(
                                                               Keys.COMMENT_JAVADOC_CLASS_MASK, 0))),
                new Boolean(Modifier.isFinal(_prefs.getInt(
                                                           Keys.COMMENT_JAVADOC_CLASS_MASK, 0))),
                new Boolean(Modifier.isPrivate(_prefs.getInt(
                                                             Keys.COMMENT_JAVADOC_CLASS_MASK, 0)))
            },
            {
                "Constructors",
                new Boolean(Modifier.isPublic(_prefs.getInt(
                                                            Keys.COMMENT_JAVADOC_CTOR_MASK, 0))),
                new Boolean(Modifier.isProtected(_prefs.getInt(
                                                               Keys.COMMENT_JAVADOC_CTOR_MASK, 0))),
                new Boolean(Modifier.isFinal(_prefs.getInt(
                                                           Keys.COMMENT_JAVADOC_CTOR_MASK, 0))),
                new Boolean(Modifier.isPrivate(_prefs.getInt(
                                                             Keys.COMMENT_JAVADOC_CTOR_MASK, 0)))
            },
            {
                "Methods",
                new Boolean(Modifier.isPublic(_prefs.getInt(
                                                            Keys.COMMENT_JAVADOC_METHOD_MASK, 0))),
                new Boolean(Modifier.isProtected(_prefs.getInt(
                                                               Keys.COMMENT_JAVADOC_METHOD_MASK, 0))),
                new Boolean(Modifier.isFinal(_prefs.getInt(
                                                           Keys.COMMENT_JAVADOC_METHOD_MASK, 0))),
                new Boolean(Modifier.isPrivate(_prefs.getInt(
                                                             Keys.COMMENT_JAVADOC_METHOD_MASK, 0)))
            },
            {
                "Variables",
                new Boolean(Modifier.isPublic(_prefs.getInt(
                                                            Keys.COMMENT_JAVADOC_VARIABLE_MASK, 0))),
                new Boolean(Modifier.isProtected(_prefs.getInt(
                                                               Keys.COMMENT_JAVADOC_VARIABLE_MASK, 0))),
                new Boolean(Modifier.isFinal(_prefs.getInt(
                                                           Keys.COMMENT_JAVADOC_VARIABLE_MASK, 0))),
                new Boolean(Modifier.isPrivate(_prefs.getInt(
                                                             Keys.COMMENT_JAVADOC_VARIABLE_MASK, 0)))
            }
        };

        public boolean isCellEditable(int row,
                                      int col)
        {
            if (col < 1)
            {
                return false;
            }
            else
            {
                return true;
            }
        }


        public Class getColumnClass(int c)
        {
            return getValueAt(0, c).getClass();
        }


        public int getColumnCount()
        {
            return columnNames.length;
        }


        public String getColumnName(int col)
        {
            return columnNames[col];
        }


        public int getRowCount()
        {
            return data.length;
        }


        public void setValueAt(Object value,
                               int    row,
                               int    col)
        {
            if (data[0][col] instanceof Integer)
            {
                // if we don't do something like this, the column
                // switches to contain
                try
                {
                    data[row][col] = new Integer((String)value);
                    fireTableCellUpdated(row, col);
                }
                catch (NumberFormatException e)
                {
                    ;
                }
            }
            else
            {
                data[row][col] = value;
                fireTableCellUpdated(row, col);
            }
        }


        public Object getValueAt(int row,
                                 int col)
        {
            return data[row][col];
        }
    }


    private class CtorTemplatePanel
        extends JPanel
        implements TemplatePanel
    {
        JTextArea bottomTextArea;
        JTextArea exceptionTextArea;
        JTextArea parameterTextArea;
        JTextArea topTextArea;
        Preferences prefs = Preferences.getInstance();

        public CtorTemplatePanel()
        {
            GridBagLayout layout = new GridBagLayout();
            setLayout(layout);

            GridBagConstraints c = new GridBagConstraints();
            JLabel topLabel = new JLabel("Top:");
            SwingHelper.setConstraints(c, 0, 0, GridBagConstraints.REMAINDER, 1,
                                       0.0, 0.0, GridBagConstraints.NORTHWEST,
                                       GridBagConstraints.NONE, c.insets, 0, 0);
            layout.setConstraints(topLabel, c);
            add(topLabel);
            this.topTextArea = new JTextArea(getTopTemplate());

            JScrollPane topScrollPane = new JScrollPane(this.topTextArea);
            SwingHelper.setConstraints(c, 0, 1, GridBagConstraints.REMAINDER, 1,
                                       1.0, 0.2, GridBagConstraints.NORTHWEST,
                                       GridBagConstraints.BOTH, c.insets, 0, 0);
            layout.setConstraints(topScrollPane, c);
            add(topScrollPane);

            JLabel paramLabel = new JLabel("Parameter:");
            c.insets.top = 3;
            SwingHelper.setConstraints(c, 0, 2, GridBagConstraints.REMAINDER, 1,
                                       0.0, 0.0, GridBagConstraints.NORTHWEST,
                                       GridBagConstraints.NONE, c.insets, 0, 0);
            layout.setConstraints(paramLabel, c);
            add(paramLabel);
            this.parameterTextArea = new JTextArea(getParameterTemplate());
            c.insets.top = 0;
            SwingHelper.setConstraints(c, 0, 3, GridBagConstraints.REMAINDER, 1,
                                       1.0, 0.1, GridBagConstraints.NORTHWEST,
                                       GridBagConstraints.BOTH, c.insets, 0, 0);

            JScrollPane parameterScrollPane = new JScrollPane(this.parameterTextArea);
            layout.setConstraints(parameterScrollPane, c);
            add(parameterScrollPane);

            JLabel exceptionLabel = new JLabel("Exception:");
            c.insets.top = 3;
            SwingHelper.setConstraints(c, 0, 4, GridBagConstraints.REMAINDER, 1,
                                       1.0, 0.0, GridBagConstraints.NORTHWEST,
                                       GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
            layout.setConstraints(exceptionLabel, c);
            add(exceptionLabel);
            this.exceptionTextArea = new JTextArea(getExceptionTemplate());
            c.insets.top = 0;
            SwingHelper.setConstraints(c, 0, 5, GridBagConstraints.REMAINDER, 1,
                                       1.0, 0.1, GridBagConstraints.NORTHWEST,
                                       GridBagConstraints.BOTH, c.insets, 0, 0);

            JScrollPane exceptionScrollPane = new JScrollPane(this.exceptionTextArea);
            layout.setConstraints(exceptionScrollPane, c);
            add(exceptionScrollPane);

            JLabel bottomLabel = new JLabel("Bottom:");
            c.insets.top = 3;
            SwingHelper.setConstraints(c, 0, 8, GridBagConstraints.REMAINDER, 1,
                                       1.0, 0.0, GridBagConstraints.NORTHWEST,
                                       GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
            layout.setConstraints(bottomLabel, c);
            add(bottomLabel);
            this.bottomTextArea = new JTextArea(getBottomTemplate());
            c.insets.top = 0;
            SwingHelper.setConstraints(c, 0, 9, GridBagConstraints.REMAINDER, 1,
                                       1.0, 0.1, GridBagConstraints.NORTHWEST,
                                       GridBagConstraints.BOTH, c.insets, 0, 0);

            JScrollPane bottomScrollPane = new JScrollPane(this.bottomTextArea);
            layout.setConstraints(bottomScrollPane, c);
            add(bottomScrollPane);
        }

        public void store()
        {
            this.prefs.put(Keys.COMMENT_JAVADOC_TEMPLATE_CTOR_TOP,
                           StringHelper.replace(this.topTextArea.getText(),
                                                "\n", DELIMETER));
            this.prefs.put(Keys.COMMENT_JAVADOC_TEMPLATE_CTOR_PARAM,
                           StringHelper.replace(
                                                this.parameterTextArea.getText(),
                                                "\n", DELIMETER));
            this.prefs.put(Keys.COMMENT_JAVADOC_TEMPLATE_CTOR_EXCEPTION,
                           StringHelper.replace(
                                                this.exceptionTextArea.getText(),
                                                "\n", DELIMETER));
            this.prefs.put(Keys.COMMENT_JAVADOC_TEMPLATE_CTOR_BOTTOM,
                           StringHelper.replace(this.bottomTextArea.getText(),
                                                "\n", DELIMETER));
        }


        public void validateSettings()
            throws ValidationException
        {
            String topText = this.topTextArea.getText();

            if (!_matcher.matches(topText, _topTextPattern))
            {
                JOptionPane.showMessageDialog(SwingUtilities.windowForComponent(this),
                                              "Invalid text for Top fragment specified.\n" +
                                              "Valid fragments have the form \"" +
                                              _topTextPattern.getPattern() +
                                              "\"\n",
                                              "Error: Invalid Top fragment",
                                              JOptionPane.ERROR_MESSAGE);
                throw new ValidationException("invalid Top fragment text specified");
            }

            String parameterText = this.parameterTextArea.getText();

            if (!_matcher.matches(parameterText, _paramPattern))
            {
                JOptionPane.showMessageDialog(SwingUtilities.windowForComponent(this),
                                              "Invalid text for Parameter fragment specified.\n" +
                                              "Valid fragments have the form \"" +
                                              _paramPattern.getPattern() +
                                              "\"\n",
                                              "Error: Invalid Parameter fragment",
                                              JOptionPane.ERROR_MESSAGE);
                throw new ValidationException("invalid Parameter fragment text specified");
            }

            String exceptionText = this.exceptionTextArea.getText();

            if (!_matcher.matches(exceptionText, _exceptionPattern))
            {
                JOptionPane.showMessageDialog(SwingUtilities.windowForComponent(this),
                                              "Invalid text for Exception fragment specified.\n" +
                                              "Valid fragments have the form \"" +
                                              _exceptionPattern.getPattern() +
                                              "\"\n",
                                              "Error: Invalid Exception fragment",
                                              JOptionPane.ERROR_MESSAGE);
                throw new ValidationException("invalid Exception fragment text specified");
            }

            String bottomText = this.bottomTextArea.getText();

            if (!_matcher.matches(bottomText, _bottomTextPattern))
            {
                JOptionPane.showMessageDialog(SwingUtilities.windowForComponent(this),
                                              "Invalid text for Bottom fragment specified.\n" +
                                              "Valid fragments have the form \"" +
                                              _topTextPattern + "\"\n",
                                              "Error: Invalid Bottom fragment",
                                              JOptionPane.ERROR_MESSAGE);
                throw new ValidationException("invalid Bottom fragment text specified");
            }
        }


        protected String getBottomTemplate()
        {
            return StringHelper.replace(this.prefs.get(
                                                       Keys.COMMENT_JAVADOC_TEMPLATE_CTOR_BOTTOM,
                                                       Defaults.COMMENT_JAVADOC_TEMPLATE_CTOR_BOTTOM),
                                        DELIMETER, "\n");
        }


        protected String getExceptionTemplate()
        {
            return StringHelper.replace(this.prefs.get(
                                                       Keys.COMMENT_JAVADOC_TEMPLATE_CTOR_EXCEPTION,
                                                       Defaults.COMMENT_JAVADOC_TEMPLATE_CTOR_EXCEPTION),
                                        DELIMETER, "\n");
        }


        protected String getParameterTemplate()
        {
            return StringHelper.replace(this.prefs.get(
                                                       Keys.COMMENT_JAVADOC_TEMPLATE_CTOR_PARAM,
                                                       Defaults.COMMENT_JAVADOC_TEMPLATE_CTOR_PARAM),
                                        DELIMETER, "\n");
        }


        protected String getTopTemplate()
        {
            return StringHelper.replace(this.prefs.get(
                                                       Keys.COMMENT_JAVADOC_TEMPLATE_CTOR_TOP,
                                                       Defaults.COMMENT_JAVADOC_TEMPLATE_CTOR_TOP),
                                        DELIMETER, "\n");
        }
    }


    private class ListDataHandler
        implements ListDataListener
    {
        public void contentsChanged(ListDataEvent e)
        {
        }


        public void intervalAdded(ListDataEvent ev)
        {
            DefaultListModel model = (DefaultListModel)ev.getSource();
            String name = (String)model.get(ev.getIndex0());

            if (!_matcher.matches(name, _tagNamePattern))
            {
                JOptionPane.showMessageDialog(SwingUtilities.windowForComponent(JavadocPanel.this),
                                              "\"" + name +
                                              "\" is no valid tag name.\nValid names have the form \"" +
                                              _tagNamePattern.getPattern() +
                                              "\"\n", "Error: Invalid tag name",
                                              JOptionPane.ERROR_MESSAGE);
                throw new IllegalArgumentException();
            }
        }


        public void intervalRemoved(ListDataEvent e)
        {
        }
    }


    private class MethodTemplatePanel
        extends CtorTemplatePanel
    {
        JTextArea returnTextArea;

        public MethodTemplatePanel()
        {
            GridBagConstraints c = new GridBagConstraints();
            JLabel returnLabel = new JLabel("Return:");
            c.insets.top = 3;

            GridBagLayout layout = (GridBagLayout)getLayout();
            SwingHelper.setConstraints(c, 0, 6, GridBagConstraints.REMAINDER, 1,
                                       1.0, 0.0, GridBagConstraints.NORTHWEST,
                                       GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
            layout.setConstraints(returnLabel, c);
            add(returnLabel);
            this.returnTextArea = new JTextArea(StringHelper.replace(
                                                                     this.prefs.get(
                                                                                    Keys.COMMENT_JAVADOC_TEMPLATE_METHOD_RETURN,
                                                                                    Defaults.COMMENT_JAVADOC_TEMPLATE_METHOD_RETURN),
                                                                     DELIMETER,
                                                                     "\n"));
            c.insets.top = 0;
            SwingHelper.setConstraints(c, 0, 7, GridBagConstraints.REMAINDER, 1,
                                       1.0, 0.1, GridBagConstraints.NORTHWEST,
                                       GridBagConstraints.BOTH, c.insets, 0, 0);

            JScrollPane returnScrollPane = new JScrollPane(this.returnTextArea);
            layout.setConstraints(returnScrollPane, c);
            add(returnScrollPane);
        }

        public void store()
        {
            this.prefs.put(Keys.COMMENT_JAVADOC_TEMPLATE_METHOD_TOP,
                           StringHelper.replace(this.topTextArea.getText(),
                                                "\n", DELIMETER));
            this.prefs.put(Keys.COMMENT_JAVADOC_TEMPLATE_METHOD_PARAM,
                           StringHelper.replace(
                                                this.parameterTextArea.getText(),
                                                "\n", DELIMETER));
            this.prefs.put(Keys.COMMENT_JAVADOC_TEMPLATE_METHOD_EXCEPTION,
                           StringHelper.replace(
                                                this.exceptionTextArea.getText(),
                                                "\n", DELIMETER));
            this.prefs.put(Keys.COMMENT_JAVADOC_TEMPLATE_METHOD_RETURN,
                           StringHelper.replace(this.returnTextArea.getText(),
                                                "\n", DELIMETER));
            this.prefs.put(Keys.COMMENT_JAVADOC_TEMPLATE_METHOD_BOTTOM,
                           StringHelper.replace(this.bottomTextArea.getText(),
                                                "\n", DELIMETER));
        }


        public void validateSettings()
            throws ValidationException
        {
            super.validateSettings();

            String returnText = this.returnTextArea.getText();

            if (!_matcher.matches(returnText, _returnPattern))
            {
                JOptionPane.showMessageDialog(SwingUtilities.windowForComponent(this),
                                              "Invalid text for Return fragment specified.\n" +
                                              "Valid fragments have the form \"" +
                                              _returnPattern.getPattern() +
                                              "\"\n",
                                              "Error: Invalid Return fragment",
                                              JOptionPane.ERROR_MESSAGE);
                throw new ValidationException("invalid Return fragment text specified");
            }
        }


        protected String getBottomTemplate()
        {
            return StringHelper.replace(this.prefs.get(
                                                       Keys.COMMENT_JAVADOC_TEMPLATE_METHOD_BOTTOM,
                                                       Defaults.COMMENT_JAVADOC_TEMPLATE_METHOD_BOTTOM),
                                        DELIMETER, "\n");
        }


        protected String getExceptionTemplate()
        {
            return StringHelper.replace(this.prefs.get(
                                                       Keys.COMMENT_JAVADOC_TEMPLATE_METHOD_EXCEPTION,
                                                       Defaults.COMMENT_JAVADOC_TEMPLATE_METHOD_EXCEPTION),
                                        DELIMETER, "\n");
        }


        protected String getParameterTemplate()
        {
            return StringHelper.replace(this.prefs.get(
                                                       Keys.COMMENT_JAVADOC_TEMPLATE_METHOD_PARAM,
                                                       Defaults.COMMENT_JAVADOC_TEMPLATE_METHOD_PARAM),
                                        DELIMETER, "\n");
        }


        protected String getTopTemplate()
        {
            return StringHelper.replace(this.prefs.get(
                                                       Keys.COMMENT_JAVADOC_TEMPLATE_METHOD_TOP,
                                                       Defaults.COMMENT_JAVADOC_TEMPLATE_METHOD_TOP),
                                        DELIMETER, "\n");
        }
    }


    private class SimpleTemplatePanel
        extends JPanel
        implements TemplatePanel
    {
        JTextArea textArea;
        String name;

        public SimpleTemplatePanel(String name,
                                   String text)
        {
            this.name = name;
            setLayout(new BorderLayout());
            this.textArea = new JTextArea(StringHelper.replace(text, DELIMETER,
                                                               "\n"));
            add(new JScrollPane(this.textArea), BorderLayout.CENTER);
        }

        public String getText()
        {
            return this.textArea.getText();
        }


        public void store()
        {
            if (TPL_CLASS.equals(this.name))
            {
                JavadocPanel.this.prefs.put(Keys.COMMENT_JAVADOC_TEMPLATE_CLASS,
                                            StringHelper.replace(
                                                                 this.textArea.getText(),
                                                                 "\n",
                                                                 DELIMETER));
            }
            else if (TPL_INTERFACE.equals(name))
            {
                JavadocPanel.this.prefs.put(Keys.COMMENT_JAVADOC_TEMPLATE_INTERFACE,
                                            StringHelper.replace(
                                                                 this.textArea.getText(),
                                                                 "\n",
                                                                 DELIMETER));
            }
            else if (TPL_FIELD.equals(name))
            {
                JavadocPanel.this.prefs.put(Keys.COMMENT_JAVADOC_TEMPLATE_VARIABLE,
                                            StringHelper.replace(
                                                                 this.textArea.getText(),
                                                                 "\n",
                                                                 DELIMETER));
            }
        }


        public void validateSettings()
            throws ValidationException
        {
            String text = this.textArea.getText();

            if (!_matcher.matches(text, _templatePattern))
            {
                JOptionPane.showMessageDialog(SwingUtilities.windowForComponent(SimpleTemplatePanel.this),
                                              "Valid templates have the form \"" +
                                              _templatePattern.getPattern() +
                                              "\"\n", "Error: Invalid template",
                                              JOptionPane.ERROR_MESSAGE);
                throw new ValidationException("");
            }
        }
    }


    /**
     * The container that displays the different template panels.
     *
     * @since 1.0b8
     */
    private class TemplateContainer
        extends JPanel
    {
        Map panels = new HashMap(); // Map of <String>:<JPanel>
        String name;
        boolean destroyed;

        public TemplateContainer(String name)
        {
            setLayout(new BorderLayout());
            add(getTemplatePanel(name), BorderLayout.CENTER);
            this.name = name;
        }

        /**
         * Returns the name of the currently displayed panel.
         *
         * @return the name of the currently displayed panel.
         */
        public String getCurrent()
        {
            return this.name;
        }


        public void dispose()
        {
            if (!_disposed)
            {
                this.panels.clear();
                _disposed = true;
            }
        }


        public void store()
        {
            for (Iterator i = this.panels.values().iterator(); i.hasNext();)
            {
                TemplatePanel panel = (TemplatePanel)i.next();
                panel.store();
            }
        }


        /**
         * Removes the current panel from the container and add the given
         * panel.
         *
         * @param name name of the panel.
         */
        public void switchPanels(String name)
        {
            remove(0);

            JPanel panel = getTemplatePanel(name);
            add(panel, BorderLayout.CENTER);
            this.name = name;
        }


        public void validateSettings()
            throws ValidationException
        {
            for (Iterator i = this.panels.values().iterator(); i.hasNext();)
            {
                TemplatePanel panel = (TemplatePanel)i.next();
                panel.validateSettings();
            }
        }


        /**
         * Returns the template panel for the given name.
         *
         * @param name name of the template panel.
         *
         * @return the template panel for the given name.
         *
         * @throws IllegalArgumentException DOCUMENT ME!
         */
        private JPanel getTemplatePanel(String name)
        {
            if (this.panels.containsKey(name))
            {
                return (JPanel)this.panels.get(name);
            }

            if (TPL_CLASS.equals(name))
            {
                SimpleTemplatePanel panel = new SimpleTemplatePanel(name,
                                                                    JavadocPanel.this.prefs.get(
                                                                                                Keys.COMMENT_JAVADOC_TEMPLATE_CLASS,
                                                                                                Defaults.COMMENT_JAVADOC_TEMPLATE_CLASS));
                this.panels.put(name, panel);

                return panel;
            }
            else if (TPL_INTERFACE.equals(name))
            {
                SimpleTemplatePanel panel = new SimpleTemplatePanel(name,
                                                                    JavadocPanel.this.prefs.get(
                                                                                                Keys.COMMENT_JAVADOC_TEMPLATE_INTERFACE,
                                                                                                Defaults.COMMENT_JAVADOC_TEMPLATE_INTERFACE));
                this.panels.put(name, panel);

                return panel;
            }
            else if (TPL_CTOR.equals(name))
            {
                CtorTemplatePanel panel = new CtorTemplatePanel();
                this.panels.put(name, panel);

                return panel;
            }
            else if (TPL_METHOD.equals(name))
            {
                MethodTemplatePanel panel = new MethodTemplatePanel();
                this.panels.put(name, panel);

                return panel;
            }
            else if (TPL_FIELD.equals(name))
            {
                SimpleTemplatePanel panel = new SimpleTemplatePanel(name,
                                                                    JavadocPanel.this.prefs.get(
                                                                                                Keys.COMMENT_JAVADOC_TEMPLATE_VARIABLE,
                                                                                                Defaults.COMMENT_JAVADOC_TEMPLATE_VARIABLE));
                this.panels.put(name, panel);

                return panel;
            }

            throw new IllegalArgumentException("unknown template name -- " +
                                               name);
        }
    }
}
