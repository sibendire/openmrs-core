/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.validator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Form;
import org.openmrs.annotation.Handler;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * This checks a Form object to make sure that it passes all API requirements. E.g. it must have a
 * name and version, if it is retired it must have metadata about that, etc.
 */
@Handler(supports = { Form.class }, order = 50)
public class FormValidator implements Validator {
	
	/** Log for this class and subclasses */
	protected final Log log = LogFactory.getLog(getClass());
	
	/**
	 * Determines if the command object being submitted is a valid type
	 *
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	public boolean supports(Class c) {
		return c.equals(Form.class);
	}
	
	/**
	 * Checks the form object for any inconsistencies/errors
	 *
	 * @see org.springframework.validation.Validator#validate(java.lang.Object,
	 *      org.springframework.validation.Errors)
	 * @should fail validation if name is null
	 * @should fail validation if version is null
	 * @should fail validation if version does not match regex
	 * @should fail validation if retiredReason is null
	 * @should fail validation if retiredReason is empty
	 * @should pass validation if all fields are correct
	 */
	public void validate(Object obj, Errors errors) {
		Form form = (Form) obj;
		if (form == null) {
			errors.rejectValue("form", "error.general");
		} else {
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "error.name");
			
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "version", "error.null");
			
			if (form.getVersion() != null && !form.getVersion().matches("^\\d.*$")) {
				errors.rejectValue("version", "Form.version.invalid");
			}
			
			if (form.isRetired()) {
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, "retireReason", "general.retiredReason.empty");
			}
		}
	}
	
}
