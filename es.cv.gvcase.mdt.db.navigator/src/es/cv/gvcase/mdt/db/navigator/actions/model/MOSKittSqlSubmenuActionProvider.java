/***************************************************************************
 * Copyright (c) 2008 Conselleria de Infraestructuras y Transporte,
 * Generalitat de la Comunitat Valenciana . All rights reserved. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Francisco Javier Cano Muñoz (Prodevelop) - initial API implementation
 *
 ******************************************************************************/
package es.cv.gvcase.mdt.db.navigator.actions.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.datatools.modelbase.sql.accesscontrol.AuthorizationIdentifier;
import org.eclipse.datatools.modelbase.sql.accesscontrol.RoleAuthorization;
import org.eclipse.datatools.modelbase.sql.accesscontrol.User;
import org.eclipse.datatools.modelbase.sql.constraints.CheckConstraint;
import org.eclipse.datatools.modelbase.sql.constraints.ForeignKey;
import org.eclipse.datatools.modelbase.sql.constraints.PrimaryKey;
import org.eclipse.datatools.modelbase.sql.expressions.QueryExpressionDefault;
import org.eclipse.datatools.modelbase.sql.expressions.SQLExpressionsFactory;
import org.eclipse.datatools.modelbase.sql.expressions.SearchConditionDefault;
import org.eclipse.datatools.modelbase.sql.schema.IdentitySpecifier;
import org.eclipse.datatools.modelbase.sql.schema.SQLObject;
import org.eclipse.datatools.modelbase.sql.schema.SQLSchemaFactory;
import org.eclipse.datatools.modelbase.sql.schema.Sequence;
import org.eclipse.datatools.modelbase.sql.tables.Column;
import org.eclipse.datatools.modelbase.sql.tables.PersistentTable;
import org.eclipse.datatools.modelbase.sql.tables.Table;
import org.eclipse.datatools.modelbase.sql.tables.ViewTable;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.ui.action.CreateChildAction;
import org.eclipse.emf.edit.ui.action.CreateSiblingAction;
import org.eclipse.emf.edit.ui.action.StaticSelectionCommandAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;

import es.cv.gvcase.ide.navigator.provider.MOSKittSubmenuActionProvider;
import es.cv.gvcase.ide.navigator.util.NavigatorUtil;
import es.cv.gvcase.mdt.common.actions.LabelHelper;
import es.cv.gvcase.mdt.db.navigator.utils.Sqlmodel2ElementFilter;

/**
 * The Class MOSKittSqlSubmenuActionProvider.
 */
public abstract class MOSKittSqlSubmenuActionProvider extends
		MOSKittSubmenuActionProvider {

	/** The filter. */
	protected Sqlmodel2ElementFilter filter;

	/* (non-Javadoc)
	 * @see org.eclipse.ui.navigator.CommonActionProvider#init(org.eclipse.ui.navigator.ICommonActionExtensionSite)
	 */
	@Override
	public void init(ICommonActionExtensionSite site) {
		super.init(site);
		filter = new Sqlmodel2ElementFilter();
	}
	
	/**
	 * Checks if is creates the child.
	 * 
	 * @return true, if is creates the child
	 */
	protected abstract boolean isCreateChild();

	/**
	 * Generate create actions gen.
	 * 
	 * @param descriptors the descriptors
	 * @param selection the selection
	 * 
	 * @return the collection< i action>
	 */
	protected Collection<IAction> generateCreateActionsGen(
			Collection<?> descriptors, ISelection selection) {

		Collection<IAction> actions = new ArrayList<IAction>();

		if (descriptors != null) {
			for (Object descriptor : descriptors) {
				if (descriptor instanceof CommandParameter) {
					CommandParameter cp = (CommandParameter) descriptor;
					if (filter.isVisible(cp.value)
							&& shouldBeShown((SQLObject) cp.value, selection,
									isCreateChild())) {

						// Add the additional attributes if necessary
						if (cp.value instanceof AuthorizationIdentifier
								|| cp.value instanceof Table) {
							EAnnotation annotation = EcoreFactory.eINSTANCE
									.createEAnnotation();
							annotation.setSource("AdditionalAttributes");
							annotation.getDetails().put("Generated", "false");
							((SQLObject) cp.value).getEAnnotations().add(
									annotation);

							if (cp.value instanceof ViewTable) {
								QueryExpressionDefault qed = SQLExpressionsFactory.eINSTANCE
										.createQueryExpressionDefault();
								qed.setName("queryExpression");
								qed.setSQL("SELECT * FROM ...");
								((ViewTable) cp.value).setQueryExpression(qed);
							}
						} else if (cp.value instanceof CheckConstraint) {
							SearchConditionDefault scd = SQLExpressionsFactory.eINSTANCE
									.createSearchConditionDefault();
							scd.setName("searchCondition");
							((CheckConstraint) cp.value)
									.setSearchCondition(scd);
						}
						else if (cp.value instanceof Sequence) {
							IdentitySpecifier is = SQLSchemaFactory.eINSTANCE
									.createIdentitySpecifier();
							is.setName("identitySpecifier");
							((Sequence) cp.value)
									.setIdentity(is);
						}

						// Set the name of the element
						setElementName((SQLObject) cp.value, selection, isCreateChild());

						StaticSelectionCommandAction ssca = null;

						if (isCreateChild()) {
							ssca = new CreateChildAction(NavigatorUtil
									.getActiveEditor(), selection, descriptor);
						} else
							ssca = new CreateSiblingAction(NavigatorUtil
									.getActiveEditor(), selection, descriptor);

						// Now we change the text of the action
						// This is done so that we can distinguish between
						// feature and element in the menu item text
						// This will allow us to separate it in submenus as it's
						// done in the UML diagrams

						String valueText = getTypeText(cp.value);
						String featureText = getFeatureText(cp.feature);
						ssca.setText(featureText + " | " + valueText);

						// Set the image of the action

						String imagePath = "";
						try {
							imagePath = FileLocator
									.toFileURL(
											Platform
													.getBundle(
															"org.eclipse.datatools.modelbase.sql.edit")
													.getResource(
															"icons/full/obj16"))
									.getPath();
							String className = cp.value.getClass()
									.getSimpleName();
							// Remove the "Impl" part of the name
							className = className.substring(0, className
									.length() - 4);
							imagePath += className + ".gif";
							Image image = new Image(Display.getCurrent(),
									imagePath);
							ImageDescriptor imDesc = ImageDescriptor
									.createFromImage(image);
							ssca.setImageDescriptor(imDesc);
						} catch (IOException e) {
							return actions;
						}

						// Should the action be enabled?

						if (selection instanceof StructuredSelection) {

							Object selectedElement = ((StructuredSelection) selection)
									.getFirstElement();

							if (!canBeCreated(selectedElement, cp.value)) {
								ssca.setEnabled(false);
							}
						}

						actions.add(ssca);
					}
				}
			}
		}
		return actions;
	}

	/*
	 * This method checks if the element should be offered for creation by the
	 * popup menu given the current selection It's supposed the metamodel allows
	 * the creation but maybe we don't want it to happen
	 */
	/**
	 * Should be shown.
	 * 
	 * @param element the element
	 * @param selection the selection
	 * @param child the child
	 * 
	 * @return true, if successful
	 */
	private boolean shouldBeShown(SQLObject element, ISelection selection,
			boolean child) {

		if (selection instanceof StructuredSelection) {
			StructuredSelection sel = (StructuredSelection) selection;

			// Role Authorization should only be offered for creation for users
			if (element instanceof RoleAuthorization) {
				if (child) {
					if (!(sel.getFirstElement() instanceof User))
						return false;
				} else {
					if (!(((SQLObject) sel.getFirstElement()).eContainer() instanceof User))
						return false;
				}
			}
		}

		return true;
	}

	// This method is used to check if the object to be created can actually be
	// created
	/**
	 * Can be created.
	 * 
	 * @param selectedElement the selected element
	 * @param objectToBeCreated the object to be created
	 * 
	 * @return true, if successful
	 */
	private boolean canBeCreated(Object selectedElement,
			Object objectToBeCreated) {

		// If the child/sibling to be created is a primary key and there is
		// already one... return false
		if (objectToBeCreated instanceof PrimaryKey) {
			if (selectedElement instanceof PersistentTable
					&& ((PersistentTable) selectedElement).getPrimaryKey() != null) {
				return false;
			} else {
				if (((SQLObject) selectedElement).eContainer() instanceof PersistentTable) {
					PersistentTable container = (PersistentTable) ((SQLObject) selectedElement)
							.eContainer();
					if (container.getPrimaryKey() != null) {
						return false;
					}
				}
			}
		}

		return true;
	}

	// Sets the name to the new element created using the popup menu
	/**
	 * Sets the element name.
	 * 
	 * @param element the element
	 * @param selection the selection
	 * @param child the child
	 */
	private void setElementName(SQLObject element, ISelection selection,
			boolean child) {

		if (selection instanceof StructuredSelection) {
			if (element instanceof PrimaryKey) {
				setPrimaryKeyName((PrimaryKey) element,
						(StructuredSelection) selection, child);
			} else if (element instanceof Column
					|| element instanceof ForeignKey
					|| element instanceof Table 
					|| element instanceof Sequence
					|| element instanceof AuthorizationIdentifier) {
				String name = "";
				if (child) {
					SQLObject parent = (SQLObject) ((StructuredSelection) selection)
							.getFirstElement();
					name = LabelHelper.INSTANCE.findName(parent, element);
				} else {
					SQLObject parent = (SQLObject) ((SQLObject) ((StructuredSelection) selection)
							.getFirstElement()).eContainer();
					name = LabelHelper.INSTANCE.findName(parent, element);
				}
				element.setName(name);
			}
		}

		return;
	}

	/**
	 * Sets the primary key name.
	 * 
	 * @param pk the pk
	 * @param selection the selection
	 * @param child the child
	 */
	private void setPrimaryKeyName(PrimaryKey pk,
			StructuredSelection selection, boolean child) {

		PersistentTable parent = null;
		if (child) {
			parent = (PersistentTable) selection.getFirstElement();
		} else {
			parent = (PersistentTable) ((SQLObject) selection.getFirstElement())
					.eContainer();
		}
		if (parent != null) {
			String parentName = "";
			if (parent.getName() != null)
				parentName = parent.getName();
			pk.setName("PK_" + parentName);
		}
	}

	/**
	 * Gets the type text.
	 * 
	 * @param object the object
	 * 
	 * @return the type text
	 */
	private String getTypeText(Object object) {
		String text = "";
		if (object instanceof SQLObject) {
			text = ((SQLObject) object).eClass().getName();
		}
		return format(capName(text), ' ');
	}

	/**
	 * Gets the feature text.
	 * 
	 * @param feature the feature
	 * 
	 * @return the feature text
	 */
	private String getFeatureText(Object feature) {
		String text = "";
		if (feature instanceof EStructuralFeature) {
			text = ((EStructuralFeature) feature).getName();
		}
		return format(capName(text), ' ');
	}

	/**
	 * Cap name.
	 * 
	 * @param name the name
	 * 
	 * @return the string
	 */
	private String capName(String name) {
		return name.length() == 0 ? name : name.substring(0, 1).toUpperCase()
				+ name.substring(1);
	}

	/**
	 * Format.
	 * 
	 * @param name the name
	 * @param separator the separator
	 * 
	 * @return the string
	 */
	private String format(String name, char separator) {
		StringBuffer result = new StringBuffer();

		for (Iterator<String> i = parseName(name, '_').iterator(); i.hasNext();) {
			String component = i.next();
			result.append(component);
			if (i.hasNext() && component.length() > 1) {
				result.append(separator);
			}
		}
		return result.toString();
	}

	/**
	 * Parses the name.
	 * 
	 * @param sourceName the source name
	 * @param sourceSeparator the source separator
	 * 
	 * @return the list< string>
	 */
	private List<String> parseName(String sourceName, char sourceSeparator) {
		List<String> result = new ArrayList<String>();
		StringBuffer currentWord = new StringBuffer();

		int length = sourceName.length();
		boolean lastIsLower = false;

		for (int index = 0; index < length; index++) {
			char curChar = sourceName.charAt(index);
			if (Character.isUpperCase(curChar)
					|| (!lastIsLower && Character.isDigit(curChar))
					|| curChar == sourceSeparator) {
				if (lastIsLower || curChar == sourceSeparator) {
					result.add(currentWord.toString());
					currentWord = new StringBuffer();
				}
				lastIsLower = false;
			} else {
				if (!lastIsLower) {
					int currentWordLength = currentWord.length();
					if (currentWordLength > 1) {
						char lastChar = currentWord.charAt(--currentWordLength);
						currentWord.setLength(currentWordLength);
						result.add(currentWord.toString());
						currentWord = new StringBuffer();
						currentWord.append(lastChar);
					}
				}
				lastIsLower = true;
			}
			if (curChar != sourceSeparator) {
				currentWord.append(curChar);
			}
		}

		result.add(currentWord.toString());
		return result;
	}

}
