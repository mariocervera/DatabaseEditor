/*******************************************************************************
 * Copyright (c) 2008 Conselleria de Infraestructuras y Transporte, Generalitat 
 * de la Comunitat Valenciana. All rights reserved. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Mario Cervera Ubeda (Prodevelop) - initial API and implementation
 *
 ******************************************************************************/
package es.cv.gvcase.mdt.db.properties.composites;

import org.eclipse.datatools.modelbase.sql.datatypes.DataLinkDataType;
import org.eclipse.datatools.modelbase.sql.datatypes.IntegrityControlOption;
import org.eclipse.datatools.modelbase.sql.datatypes.LinkControlOption;
import org.eclipse.datatools.modelbase.sql.datatypes.ReadPermissionOption;
import org.eclipse.datatools.modelbase.sql.datatypes.SQLDataTypesPackage;
import org.eclipse.datatools.modelbase.sql.datatypes.UnlinkOption;
import org.eclipse.datatools.modelbase.sql.datatypes.WritePermissionOption;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.provider.EcoreItemProviderAdapterFactory;
import org.eclipse.emf.ecoretools.tabbedproperties.providers.TabbedPropertiesLabelProvider;
import org.eclipse.emf.ecoretools.tabbedproperties.sections.AbstractTabbedPropertySection;
import org.eclipse.emf.ecoretools.tabbedproperties.sections.widgets.CSingleObjectChooser;
import org.eclipse.emf.ecoretools.tabbedproperties.utils.TextChangeListener;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;

import es.cv.gvcase.mdt.common.composites.DetailComposite;

/**
 * The composite that hosts the widgets to manage the details of the DataLinkDataType
 * 
 * @author mcervera
 */
public class DataLinkDetailsComposite extends DetailComposite {
	
	/** A helper to listen for events that indicate that a text field has been changed. */
	private TextChangeListener textListener;
	
	/** Listen events when the check box is selected. */
	private SelectionListener checkBoxListener;
	
	/** Widgets. */
	
	private CLabel textlabel;
	
	/** The text. */
	private Text text;
	
	/** The link control label. */
	private CLabel linkControlLabel;
	
	/** The link control chooser. */
	private CSingleObjectChooser linkControlChooser;
	
	/** The integrity control label. */
	private CLabel integrityControlLabel;
	
	/** The integrity control chooser. */
	private CSingleObjectChooser integrityControlChooser;
	
	/** The read permission label. */
	private CLabel readPermissionLabel;
	
	/** The read permission chooser. */
	private CSingleObjectChooser readPermissionChooser;
	
	/** The write permission label. */
	private CLabel writePermissionLabel;
	
	/** The write permission chooser. */
	private CSingleObjectChooser writePermissionChooser;
	
	/** The check button. */
	private Button checkButton;
	
	/** The unlink option label. */
	private CLabel unlinkOptionLabel;
	
	/** The unlink option chooser. */
	private CSingleObjectChooser unlinkOptionChooser;
	
	/** The section. */
	private AbstractTabbedPropertySection section;
	
	
	/**
	 * Constructor
	 * 
	 * @param the parent composite
	 * @param the style of the composite
	 * 
	 * @author mcervera
	 */
	public DataLinkDetailsComposite(Composite parent, int style) {
		super(parent, style);
	}
	
	/**
	 * Constructor
	 * 
	 * @param the parent composite
	 * @param the style of the composite
	 * @param the section
	 * 
	 * @author mcervera
	 */
	public DataLinkDetailsComposite(Composite parent, int style, AbstractTabbedPropertySection section) {
		super(parent, style);
		
		this.section = section;
	}

	/* (non-Javadoc)
	 * @see es.cv.gvcase.mdt.common.composites.DetailComposite#createWidgets(org.eclipse.swt.widgets.Composite, org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory)
	 */
	@Override
	public void createWidgets(Composite composite, TabbedPropertySheetWidgetFactory widgetFactory) {
		
		super.createWidgets(composite, widgetFactory);
		
		this.setLayout(new GridLayout(2, false));
		
		textlabel = widgetFactory.createCLabel(this, "Length:");
		
		text = widgetFactory.createText(this, "", getStyle() | SWT.BORDER);

		if (getLengthFeature() != null) {
			boolean isChangeable = getLengthFeature().isChangeable();

			text.setEditable(isChangeable);
			text.setEnabled(isChangeable);
		}
		
		linkControlLabel = widgetFactory.createCLabel(this, "Link control:");

		linkControlChooser = new CSingleObjectChooser(this, widgetFactory, SWT.NONE);
		linkControlChooser.setChoices(getLinkControlChoices());
		linkControlChooser.setLabelProvider(getLabelProvider());
		linkControlChooser.setSection(section);

		if (getLinkControlFeature() != null) {
			linkControlChooser.setChangeable(getLinkControlFeature().isChangeable());
		}
		
		integrityControlLabel = widgetFactory.createCLabel(this, "Integrity control:");

		integrityControlChooser = new CSingleObjectChooser(this, widgetFactory, SWT.NONE);
		integrityControlChooser.setChoices(getIntegrityControlChoices());
		integrityControlChooser.setLabelProvider(getLabelProvider());
		integrityControlChooser.setSection(section);

		if (getIntegrityControlFeature() != null) {
			integrityControlChooser.setChangeable(getIntegrityControlFeature().isChangeable());
		}
		
		readPermissionLabel = widgetFactory.createCLabel(this, "Read permission:");

		readPermissionChooser = new CSingleObjectChooser(this, widgetFactory, SWT.NONE);
		readPermissionChooser.setChoices(getReadPermissionChoices());
		readPermissionChooser.setLabelProvider(getLabelProvider());
		readPermissionChooser.setSection(section);

		if (getReadPermissionFeature() != null) {
			readPermissionChooser.setChangeable(getReadPermissionFeature().isChangeable());
		}
		
		writePermissionLabel = widgetFactory.createCLabel(this, "Write permission:");

		writePermissionChooser = new CSingleObjectChooser(this, widgetFactory, SWT.NONE);
		writePermissionChooser.setChoices(getWritePermissionChoices());
		writePermissionChooser.setLabelProvider(getLabelProvider());
		writePermissionChooser.setSection(section);

		if (getWritePermissionFeature() != null) {
			writePermissionChooser.setChangeable(getWritePermissionFeature().isChangeable());
		}
		
		checkButton = widgetFactory.createButton(this, "Is recovery", SWT.CHECK);

		if (getRecoveryFeature() != null) {
			checkButton.setEnabled(getRecoveryFeature().isChangeable());
		}
		
		unlinkOptionLabel = widgetFactory.createCLabel(this, "Unlink option:");

		unlinkOptionChooser = new CSingleObjectChooser(this, widgetFactory, SWT.NONE);
		unlinkOptionChooser.setChoices(getUnlinkOptionChoices());
		unlinkOptionChooser.setLabelProvider(getLabelProvider());
		unlinkOptionChooser.setSection(section);

		if (getUnlinkOptionFeature() != null) {
			unlinkOptionChooser.setChangeable(getUnlinkOptionFeature().isChangeable());
		}
	}

	/* (non-Javadoc)
	 * @see es.cv.gvcase.mdt.common.composites.DetailComposite#setSectionData(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void setSectionData(Composite composite) {
		
		super.setSectionData(composite);
		
		GridData data = new GridData();
		data.widthHint = getStandardLabelWidth(composite, new String[] { "Length:" });
		textlabel.setLayoutData(data);

		data = new GridData(GridData.FILL_HORIZONTAL);
		text.setLayoutData(data);
		
		data = new GridData();
		data.widthHint = getStandardLabelWidth(composite, new String[] { "Link control:" });
		linkControlLabel.setLayoutData(data);

		data = new GridData(GridData.FILL_HORIZONTAL);
		linkControlChooser.setLayoutData(data);
		
		data = new GridData();
		data.widthHint = getStandardLabelWidth(composite, new String[] { "Integrity control:" });
		integrityControlLabel.setLayoutData(data);

		data = new GridData(GridData.FILL_HORIZONTAL);
		integrityControlChooser.setLayoutData(data);
		
		data = new GridData();
		data.widthHint = getStandardLabelWidth(composite, new String[] { "Read Permission:" });
		readPermissionLabel.setLayoutData(data);

		data = new GridData(GridData.FILL_HORIZONTAL);
		readPermissionChooser.setLayoutData(data);
		
		data = new GridData();
		data.widthHint = getStandardLabelWidth(composite, new String[] { "Write Permission:" });
		writePermissionLabel.setLayoutData(data);

		data = new GridData(GridData.FILL_HORIZONTAL);
		writePermissionChooser.setLayoutData(data);
		
		data = new GridData();
		data.horizontalSpan = 2;
		checkButton.setLayoutData(data);
		
		data = new GridData();
		data.widthHint = getStandardLabelWidth(composite, new String[] { "Unlink option:" });
		unlinkOptionLabel.setLayoutData(data);

		data = new GridData(GridData.FILL_HORIZONTAL);
		unlinkOptionChooser.setLayoutData(data);
		
	}

	/* (non-Javadoc)
	 * @see es.cv.gvcase.mdt.common.composites.DetailComposite#hookListeners()
	 */
	@Override
	public void hookListeners() {
		
		super.hookListeners();
		
		textListener = new TextChangeListener() {

			public void textChanged(Control control) {
				handleTextModified();
			}
		};
		textListener.startListeningTo(text);
		
		checkBoxListener = new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				handleCheckButtonModified();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				// Do nothing
			}
		};
		
		checkButton.addSelectionListener(checkBoxListener);
		
		linkControlChooser.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				handleLinkControlModified();
			}
		});
		
		integrityControlChooser.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				handleIntegrityControlModified();
			}
		});
		
		readPermissionChooser.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				handleReadPermissionModified();
			}
		});
		
		writePermissionChooser.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				handleWritePermissionModified();
			}
		});
		
		unlinkOptionChooser.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				handleUnlinkOptionModified();
			}
		});
		
		
	}

	/* (non-Javadoc)
	 * @see es.cv.gvcase.mdt.common.composites.DetailComposite#loadData()
	 */
	@Override
	public void loadData() {
		
		refresh();
	}
	
	public void refresh() {
		
		if(text != null) {
			text.setText(Integer.toString(getElementLength()));
		}
		if(checkButton != null) {
			checkButton.setSelection(getElementRecovery());
		}
		if(linkControlChooser != null) {
			LinkControlOption lco = getElementLinkControl();
			String s = lco.equals(LinkControlOption.FILE_LINK_CONTROL_LITERAL) ? "FILE LINK CONTROL" : "NO FILE LINK CONTROL";
			linkControlChooser.setSelection(s);
		}
		if(integrityControlChooser != null) {
			integrityControlChooser.setSelection(getElementIntegrityControl());
		}
		if(readPermissionChooser != null) {
			ReadPermissionOption rpo = getElementReadPermission();
			String s = rpo.equals(ReadPermissionOption.DB_LITERAL) ? "DATABASE" : "FILE SYSTEM";
			readPermissionChooser.setSelection(s);
		}
		if(writePermissionChooser != null) {
			WritePermissionOption wpo = getElementWritePermission();
			if(wpo.equals(WritePermissionOption.ADMIN_LITERAL)) {
				writePermissionChooser.setSelection("ADMINISTRATOR");
			}
			else if(wpo.equals(WritePermissionOption.BLOCKED_LITERAL)) {
				writePermissionChooser.setSelection("BLOCKED");
			}
			else if(wpo.equals(WritePermissionOption.FS_LITERAL)) {
				writePermissionChooser.setSelection("FILE SYSTEM");
			}
				
		}
		if(unlinkOptionChooser != null) {
			unlinkOptionChooser.setSelection(getElementUnlinkOption());
		}
	}

	
	/**
	 * Handle the modify event of the text widget
	 * 
	 * @author mcervera
	 */
	protected void handleTextModified() {
		
		if(getEMFEditDomain() == null) return;
		
		String text = this.text.getText();
		
		if(isInteger(text)) {
			if(getElement() != null && getLengthFeature() != null) {
				SetCommand command = (SetCommand) SetCommand.create(getEMFEditDomain(),
						getElement(), getLengthFeature(), new Integer(text));
				getEMFEditDomain().getCommandStack().execute(command);
			}
		}
		else {
			writeErrorMessageOnStatusBar("The length must be an integer number");
			refresh();
		}
		
	}
	
	/**
	 * Handle the linkControlChooser widget selection
	 * 
	 * @author mcervera
	 */
	private void handleLinkControlModified() {
		
		if(getEMFEditDomain() == null) return;
		
		LinkControlOption lco = null;
		
		String s = (String)linkControlChooser.getSelection();
		
		if(s.equals("FILE LINK CONTROL")) {
			lco = LinkControlOption.FILE_LINK_CONTROL_LITERAL;
		}
		else if(s.equals("NO FILE LINK CONTROL")){
			lco = LinkControlOption.NO_FILE_LINK_CONTROL_LITERAL;
		}
		
		if(lco != null) {
			SetCommand command = (SetCommand) SetCommand.create(getEMFEditDomain(),
					getElement(), getLinkControlFeature(), lco);
			getEMFEditDomain().getCommandStack().execute(command);
		}
	}
	
	/**
	 * Handle the integrityControlChooser widget selection
	 * 
	 * @author mcervera
	 */
	private void handleIntegrityControlModified() {
		
		if(getEMFEditDomain() == null) return;
		
		IntegrityControlOption ico = (IntegrityControlOption)integrityControlChooser.getSelection();
		
		SetCommand command = (SetCommand) SetCommand.create(getEMFEditDomain(),
				getElement(), getIntegrityControlFeature(), ico);
		getEMFEditDomain().getCommandStack().execute(command);
	}
	
	/**
	 * Handle the readPermissionChooser widget selection
	 * 
	 * @author mcervera
	 */
	private void handleReadPermissionModified() {
		
		if(getEMFEditDomain() == null) return;
		
		ReadPermissionOption rpo = null;
		
		String s = (String)readPermissionChooser.getSelection();
		
		if(s.equals("DATABASE")) {
			rpo = ReadPermissionOption.DB_LITERAL;
		}
		else if(s.equals("FILE SYSTEM")) {
			rpo = ReadPermissionOption.FS_LITERAL;
		}
		
		if(rpo != null) {
			SetCommand command = (SetCommand) SetCommand.create(getEMFEditDomain(),
					getElement(), getReadPermissionFeature(), rpo);
			getEMFEditDomain().getCommandStack().execute(command);
		}
	}

	/**
	 * Handle the writePermissionChooser widget selection
	 * 
	 * @author mcervera
	 */
	private void handleWritePermissionModified() {
	
		if(getEMFEditDomain() == null) return;
		
		WritePermissionOption wpo = null;
		
		String s = (String) writePermissionChooser.getSelection();
		
		if(s.equals("ADMINISTRATOR")) {
			wpo = WritePermissionOption.ADMIN_LITERAL;
		}
		else if(s.equals("BLOCKED")) {
			wpo = WritePermissionOption.BLOCKED_LITERAL;
		}
		else if(s.equals("FILE SYSTEM")) {
			wpo = WritePermissionOption.FS_LITERAL;
		}
		
		if(wpo != null) {
			SetCommand command = (SetCommand) SetCommand.create(getEMFEditDomain(),
					getElement(), getWritePermissionFeature(), wpo);
			getEMFEditDomain().getCommandStack().execute(command);
		}
	}
	
	/**
	 * Handle the check button widget selection
	 * 
	 * @author mcervera
	 */
	private void handleCheckButtonModified() {
		
		if(getEMFEditDomain() == null) return;
		
		boolean newValue = checkButton.getSelection();
		
		SetCommand command = (SetCommand) SetCommand.create(getEMFEditDomain(),
				getElement(), getRecoveryFeature(), new Boolean(newValue));
		getEMFEditDomain().getCommandStack().execute(command);
	}
	
	/**
	 * Handle the unlinkOptionChooser widget selection
	 * 
	 * @author mcervera
	 */
	private void handleUnlinkOptionModified() {
		
		if(getEMFEditDomain() == null) return;
		
		UnlinkOption uo = (UnlinkOption)unlinkOptionChooser.getSelection();
		
		SetCommand command = (SetCommand) SetCommand.create(getEMFEditDomain(),
				getElement(), getUnlinkOptionFeature(), uo);
		getEMFEditDomain().getCommandStack().execute(command);
	}
	
	
	private EStructuralFeature getLengthFeature() {
		return SQLDataTypesPackage.eINSTANCE.getDataLinkDataType_Length();
	}
	
	
	private EStructuralFeature getLinkControlFeature() {
		return SQLDataTypesPackage.eINSTANCE.getDataLinkDataType_LinkControl();
	}
	
	
	private EStructuralFeature getIntegrityControlFeature() {
		return SQLDataTypesPackage.eINSTANCE.getDataLinkDataType_IntegrityControl();
	}
	
	
	private EStructuralFeature getReadPermissionFeature() {
		return SQLDataTypesPackage.eINSTANCE.getDataLinkDataType_ReadPermission();
	}
	
	
	private EStructuralFeature getWritePermissionFeature() {
		return SQLDataTypesPackage.eINSTANCE.getDataLinkDataType_WritePermission();
	}
	
	
	private EStructuralFeature getRecoveryFeature() {
		return SQLDataTypesPackage.eINSTANCE.getDataLinkDataType_Recovery();
	}
	
	
	private EStructuralFeature getUnlinkOptionFeature() {
		return SQLDataTypesPackage.eINSTANCE.getDataLinkDataType_Unlink();
	}
	
	
	/**
	 * Gets the property length of the selected DataLinkDataType
	 * 
	 * @return the element length
	 * 
	 * @author mcervera
	 */
	private int getElementLength() {
		if(getElement() != null && getElement() instanceof DataLinkDataType) {
			return ((DataLinkDataType)getElement()).getLength();
		}
		
		return -1;
	}
	
	/**
	 * Gets the property linkControl of the selected DataLinkDataType
	 * 
	 * @return the element linkControl
	 * 
	 * @author mcervera
	 */
	private LinkControlOption getElementLinkControl() {
		if(getElement() != null && getElement() instanceof DataLinkDataType) {
			return ((DataLinkDataType)getElement()).getLinkControl();
		}
		
		return null;
	}
	
	/**
	 * Gets the property integrityControl of the selected DataLinkDataType
	 * 
	 * @return the element integrityControl
	 * 
	 * @author mcervera
	 */
	private IntegrityControlOption getElementIntegrityControl() {
		if(getElement() != null && getElement() instanceof DataLinkDataType) {
			return ((DataLinkDataType)getElement()).getIntegrityControl();
		}
		
		return null;
	}
	
	/**
	 * Gets the property readPermission of the selected DataLinkDataType
	 * 
	 * @return the element readPermission
	 * 
	 * @author mcervera
	 */
	private ReadPermissionOption getElementReadPermission() {
		if(getElement() != null && getElement() instanceof DataLinkDataType) {
			return ((DataLinkDataType)getElement()).getReadPermission();
		}
		
		return null;
	}
	
	/**
	 * Gets the property writePermission of the selected DataLinkDataType
	 * 
	 * @return the element writePermission
	 * 
	 * @author mcervera
	 */
	private WritePermissionOption getElementWritePermission() {
		if(getElement() != null && getElement() instanceof DataLinkDataType) {
			return ((DataLinkDataType)getElement()).getWritePermission();
		}
		
		return null;
	}
	
	/**
	 * Gets the property recovery of the selected DataLinkDataType
	 * 
	 * @return the element recovery
	 * 
	 * @author mcervera
	 */
	private boolean getElementRecovery() {
		if(getElement() != null && getElement() instanceof DataLinkDataType) {
			return ((DataLinkDataType)getElement()).isRecovery();
		}
		
		return false;
	}
	
	/**
	 * Gets the property unlink of the selected DataLinkDataType
	 * 
	 * @return the element unlink
	 * 
	 * @author mcervera
	 */
	private UnlinkOption getElementUnlinkOption() {
		if(getElement() != null && getElement() instanceof DataLinkDataType) {
			return ((DataLinkDataType)getElement()).getUnlink();
		}
		
		return null;
	}
	
	/**
	 * Checks if the String passed as parameter is of type Integer.
	 * 
	 * @param s, the string to be checked
	 * 
	 * @return true, if is integer
	 * 
	 * @author mcervera
	 */
	private boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
			return true;
		}
		catch(Exception e) {
			return false;
		}
	}
	
	protected ILabelProvider getLabelProvider() {
		return new TabbedPropertiesLabelProvider(new EcoreItemProviderAdapterFactory());
	}
	
	/**
	 * Gets the link control choices.
	 * 
	 * @return the link control choices
	 * 
	 * @author mcervera
	 */
	private Object[] getLinkControlChoices() {
		Object[] result = new Object[2];
		
		result[0] = "FILE LINK CONTROL";
		result[1] = "NO FILE LINK CONTROL";
		
		return result;
	}
	
	/**
	 * Gets the integrity control choices.
	 * 
	 * @return the integrity control choices
	 * 
	 * @author mcervera
	 */
	private Object[] getIntegrityControlChoices() {
		Object[] result = new Object[3];
		
		result[0] = IntegrityControlOption.NONE_LITERAL;
		result[1] = IntegrityControlOption.ALL_LITERAL;
		result[2] = IntegrityControlOption.SELECTIVE_LITERAL;
		
		return result;
	}
	
	/**
	 * Gets the read permission choices.
	 * 
	 * @return the read permission choices
	 * 
	 * @author mcervera
	 */
	private Object[] getReadPermissionChoices() {
		Object[] result = new Object[2];
		
		result[0] = "DATABASE";
		result[1] = "FILE SYSTEM";
		
		return result;
	}
	
	/**
	 * Gets the write permission choices.
	 * 
	 * @return the write permission choices
	 * 
	 * @author mcervera
	 */
	private Object[] getWritePermissionChoices() {
		Object[] result = new Object[3];
		
		result[0] = "ADMINISTRATOR";
		result[1] = "BLOCKED";
		result[2] = "FILE SYSTEM";
		
		return result;
	}
	
	/**
	 * Gets the unlink option choices.
	 * 
	 * @return the unlink option choices
	 * 
	 * @author mcervera
	 */
	private Object[] getUnlinkOptionChoices() {
		Object[] result = new Object[3];
		
		result[0] = UnlinkOption.RESTORE_LITERAL;
		result[1] = UnlinkOption.NONE_LITERAL;
		result[2] = UnlinkOption.DELETE_LITERAL;
		
		return result;
	}
	
}

