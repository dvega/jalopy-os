/*
 * Copyright 2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * $Header$
 */
package org.apache.beehive.netui.pageflow.annotations;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.lang.annotation.RetentionPolicy.SOURCE;

    public enum NavigateTo
    {
        currentPage,
        previousPage,
        previousAction,
        page
    }

// This will never compile but it looks like good code to test
public @interface Trademark {
	String description();
	String owner();
}

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.PACKAGE})

public @interface License {
	String name();
	String notice();
	boolean redistributable();
	Trademark[] trademarks();
}

/**
* @author:me
*/
public class Misc extends Object
{

@License(
name = "Apache",
notice = "license notice ........",
redistributable = true,
trademarks =
   {@Trademark(description="abcd",owner="xyz"),
    @Trademark(description="efgh",owner="klmn")}
)

class Example2 {

    public static void main(String []args) {
    License lic;
    lic=Example2.class.getAnnotation(License.class);
    System.out.println(lic.name());
    System.out.println(lic.notice());
    System.out.println(lic.redistributable());
    Trademark [] tms = lic.trademarks();
	for(int x=0;x<10;x++) {
		System.out.println(x);
	}
    for(Trademark tm : tms) {
        System.out.println(tm.description());
        System.out.println(tm.owner());
        }
    }
}

    public void foo()
    {
        int result = 12 + 4 % 3 * 7 / 8;
        logger.debug("some message: " + someVar);
		List<? extends FOO, BAR<T>> list = new ArrayList<? extends FOO>();
        int[] a;
        int b[];

        int[][] c = new int[0][2];
    }
    public void barr(List<? extends FOO> list)
    {
        int result = 12 + 4 % 3 * 7 / 8;
        logger.debug("some message: " + someVar);
		List<? extends FOO, BAR<T>> list = new ArrayList<? extends FOO>();
        int[] a;
        int b[];

        int[][] c = new int[0][2];
    }
	public static Set<T> unmodifiableSet(Set<T> set) {
	    int on = -1;
	}
	class ScrollPane<MyPane extends Pane> {
		String pain = "yes";
	}
	public @interface SampleAnnotation{
      	String someValue;
} 
public @interface NormalAnnotation{
	String value1;
	int value2;
}
}



/**
 * Holds jpf annotations to provide a Jpf prefix and local scoping of names.
 */
public interface Jpf
{
    /**
     * Jpf.Controller; was jpf:controller
     */
    @Target( TYPE )
    @Retention( RUNTIME )
    public @interface Controller
    {
        SharedFlowRef[] sharedFlowRefs() default {};
        
        /**
         * Location of the Struts merge file; relative to the page flow, or a path from the webapp root.  Optional.
         */
        String strutsMerge() default "";
        
        /**
         * Location of the ValidatorPlugIn merge file; relative to the page flow, or a path from the webapp root.
         * Optional.
         */
        String validatorMerge() default "";
        
        /**
         * List of additional webapp-relative file paths to be added to the 'pathnames' property of the ValidatorPlugIn
         * init.
         */ 
        String[] customValidatorConfigs() default {};

        /**
         * Location of the Tiles Definitions XML files; relative to the page flow, or a path from the webapp root.
         * For multiple definition files are allowed.
         * Optional.
         */
        String[] tilesDefinitionsConfigs() default {};

        /**
         * is pageflow nested (optional )
         */
        boolean nested() default false;

        /**
         * is pageflow long-lived (optional )
         */
        boolean longLived() default false;

        /**
         * roles allowed to access actions in this page flow (optional )
         */
        String[] rolesAllowed() default {};

        /**
         * is login required to access actions in this page flow (optional )
         */
        boolean loginRequired() default false;

        /**
         * is the pageflow read-only, i.e., are its actions guaranteed not to modify member
         * state (optional )
         */
        boolean readOnly() default false;

        /**
         * global forwards (optional )
         */
        Forward[] forwards() default {};

        /**
         * exception catches (optional )
         */
        Catch[] catches() default {};
        
        /**
         * simple actions (optional)
         */ 
        SimpleAction[] simpleActions() default {};

        /**
         * validation rules on a per-bean (class) basis (optional )
         */
        ValidatableBean[] validatableBeans() default {};

        MessageBundle[] messageBundles() default {};
        
        MultipartHandler multipartHandler() default MultipartHandler.disabled;
    }
    
    public enum MultipartHandler
    {
        disabled,
        memory,
        disk
    }
    
    @Target( ANNOTATION_TYPE )
    @Retention( RUNTIME )
    public @interface ConditionalForward
    {
        /**
         * The expression that will trigger this forward (required).
         */
        String condition();
        
        /**
         * The forward name (optional).
         */ 
        String name() default "";
        
        /**
         * The forward path.  Mutually-exclusive with {@link #navigateTo}, {@link #returnAction}, {@link #action},
         * and {@link #tilesDefinition}.
         */ 
        String path() default "";
        
        /**
         * The action to be invoked on the calling page flow.  Mutually-exclusive with {@link #path},
         * {@link #navigateTo}, {@link #action}, and {@link #tilesDefinition}, and only valid in a nested page flow
         * ({@link Controller#nested} must be <code>true</code>).
         */ 
        String returnAction() default "";
        
        /**
         * A symbolic name for the page/action to which to navigate.  Mutually-exclusive with {@link #path},
         * {@link #returnAction}, {@link #action}, and {@link #tilesDefinition}.
         */
        NavigateTo navigateTo() default NavigateTo.currentPage;
        
        /**
         * An action to forward to.  Mutually-exclusive with {@link #path}, {@link #navigateTo}, {@link #returnAction},
         * and {@link #tilesDefinition}.
         */ 
        String action() default "";

        /**
         * A Tiles definition to forward to.  Mutually-exclusive with {@link #path}, {@link #navigateTo},
         * {@link #returnAction}, and {@link #action}.
         */
        String tilesDefinition() default "";

        /**
         * Tells whether the original query string will be restored on a rerun of a previous action.  Only valid when
         * <code>navigateTo</code> is <code>NavigateTo.previousAction</code>.
         * @return boolean
         */ 
        boolean restoreQueryString() default false;
        
        Class outputFormBeanType() default Void.class;
        String outputFormBean() default "";
        boolean redirect() default false; // optional
        boolean externalRedirect() default false; // optional
    }
    
    /**
     * was jpf:view-properties
     */ 
    @Target( TYPE )
    @Retention( SOURCE )
    public @interface ViewProperties
    {
        String[] value() default {};
    }
    
    /**
     * was jpf:action
     */
    @Target( METHOD )
    @Retention( RUNTIME )
    public @interface Action
    {
        /**
         * page flow-scoped form; references a member variable (bean) in the page flow (optional )
         */
        String useFormBean() default "";
        
        /**
         * is this action read-only, i.e., is it guaranteed not to modify member state (optional )
         */
        boolean readOnly() default false;
        
        /**
         * roles allowed to access this action (optional )
         */
        String[] rolesAllowed() default {};
        
        /**
         * is login required to access this action (optional )
         */
        boolean loginRequired() default false;
        
        /**
         * forwards (optional )
         */
        Forward[] forwards() default {};
        
        /**
         * exception catches (optional )
         */
        Catch[] catches() default {};
        
        /**
         * field-level validation rules tied to the action (optional )
         */
        ValidatableProperty[] validatableProperties() default {};
        
        /**
         * forward used when validation fails
         */
        Forward validationErrorForward() default @Jpf.Forward( name="" );
        
        /**
         * Turn form validation on or off for this action.
         * If {@link #validationErrorForward()} is not empty, this value is set to <code>true</code>automatically.
         */
        boolean doValidation() default false;
        
        /**
         * Prevent multiple submits to this action.  If multiple submits occur, a
         * {@link org.apache.beehive.netui.pageflow.DoubleSubmitException} is thrown.
         */ 
        boolean preventDoubleSubmit() default false;
    }
    
    @Target( ANNOTATION_TYPE )
    @Retention( RUNTIME )
    public @interface SimpleAction
    {
        /**
         * name (required)
         */ 
        String name();
        
        ConditionalForward[] conditionalForwards() default {};
        
        /**
         * page flow-scoped form; references a member variable (bean) in the page flow
         * (optional )
         */
        String useFormBean() default "";

        /**
         * is this action read-only, i.e., is it guaranteed not to modify member state
         * (optional )
         */
        boolean readOnly() default false;

        /**
         * roles allowed to access this action (optional )
         */
        String[] rolesAllowed() default {};

        /**
         * is login required to access this action (optional )
         */
        boolean loginRequired() default false;

        /**
         * exception catches (optional )
         */
        Catch[] catches() default {};

        /**
         * field-level validation rules tied to the action (optional )
         */
        ValidatableProperty[] validatableProperties() default {};

        /**
         * forward used when validation fails
         */
        Forward validationErrorForward() default @Jpf.Forward( name="" );
        
        /**
         * Turn form validation on or off for this action.
         * If {@link #validationErrorForward()} is not empty, this value is set to <code>true</code>automatically.
         */
        boolean doValidation() default false;
        
        /**
         * The forward path.  Mutually-exclusive with {@link #navigateTo}, {@link #returnAction}, {@link #action},
         * {@link #tilesDefinition}, and {@link #forwardRef}.
         */ 
        String path() default "";
        
        /**
         * The action to be invoked on the calling page flow.  Mutually-exclusive with {@link #path},
         * {@link #navigateTo}, {@link #action}, {@link #tilesDefinition}, and {@link #forwardRef}, and only valid in
         * a nested page flow ({@link Controller#nested} must be <code>true</code>).
         */ 
        String returnAction() default "";
        
        /**
         * A symbolic name for the page/action to which to navigate.  Mutually-exclusive with {@link #path},
         * {@link #returnAction}, {@link #action}, {@link #tilesDefinition}, and {@link #forwardRef}..
         */
        NavigateTo navigateTo() default NavigateTo.currentPage;
        
        /**
         * An action to forward to.  Mutually-exclusive with {@link #path}, {@link #navigateTo}, {@link #returnAction},
         * {@link #tilesDefinition}, and {@link #forwardRef}..
         */ 
        String action() default "";
        
        /**
         * A Tiles definition to forward to.  Mutually-exclusive with {@link #path}, {@link #navigateTo},
         * {@link #returnAction}, {@link #action}, and {@link #forwardRef}..
         */
        String tilesDefinition() default "";
        
        /**
         * The name of a class-level forward ({@link Forward}).  Mutually-exclusive with with {@link #path},
         * {@link #navigateTo}, {@link #returnAction}, {@link #tilesDefinition}, and {@link #forwardRef}.
         */
        String forwardRef() default "";

        /**
         * Tells whether the original query string will be restored on a rerun of a previous action.  Only valid when
         * <code>navigateTo</code> is <code>NavigateTo.previousAction</code>.
         * @return boolean
         */ 
        boolean restoreQueryString() default false;
        
        Class outputFormBeanType() default Void.class;
        String outputFormBean() default "";
        boolean redirect() default false; // optional
        boolean externalRedirect() default false; // optional
        
        /**
         * Prevent multiple submits to this action.  If multiple submits occur, a
         * {@link org.apache.beehive.netui.pageflow.DoubleSubmitException} is thrown.
         */ 
        boolean preventDoubleSubmit() default false;
    }

    /**
     * was jpf:catch
     */
    @Target( ANNOTATION_TYPE )
    @Retention( RUNTIME )
    public @interface Catch
    {
        /**
         * the type of exception to catch (required )
         */
        Class<? extends Throwable> type();

        /**
         * the Jpf.ExceptionHandler method to invoke when the exception occurs (optional )
         * (cannot be present if path is present )
         */
        String method() default"";

        /**
         * the destination URI when the exception occurs (cannot be present if method is
         * present) (optional )
         */
        String path() default "";

        /**
         * the literal message to send to the exception handler method (optional )
         */
        String message() default "";

        /**
         * a message-resources key to lookup the message that will be sent to the exception
         * handler method (optional )
         */
        String messageKey() default "";
    }

    /**
     * was jpf:exception-handler
     */
    @Target( METHOD )
    @Retention( RUNTIME )
    public @interface ExceptionHandler
    {
        /**
         * forwards (optional )
         */
        Forward[] forwards() default {};

        /**
         * is the exception handler read-only, i.e., is it guaranteed not to modify member
         * state (optional )
         */
        boolean readOnly() default false;
    }

    /**
     * was jpf:forward
     */
    @Target( ANNOTATION_TYPE )
    @Retention( RUNTIME )
    public @interface Forward
    {
        /**
         * the forward name (required )
         */
        String name();
        
        /**
         * The forward path.  Mutually-exclusive with {@link #navigateTo}, {@link #returnAction}, {@link #action},
         * and {@link #tilesDefinition}.
         */ 
        String path() default "";
        
        /**
         * The action to be invoked on the calling page flow.  Mutually-exclusive with {@link #path},
         * {@link #navigateTo}, {@link #action}, and {@link #tilesDefinition}, and only valid in a nested page flow
         * ({@link Controller#nested} must be <code>true</code>).
         */ 
        String returnAction() default "";
        
        /**
         * A symbolic name for the page/action to which to navigate.  Mutually-exclusive with {@link #path},
         * {@link #returnAction}, {@link #action}, and {@link #tilesDefinition}.
         */
        NavigateTo navigateTo() default NavigateTo.currentPage;
        
        /**
         * An action to forward to.  Mutually-exclusive with {@link #path}, {@link #navigateTo}, {@link #returnAction},
         * and {@link #tilesDefinition}.
         */ 
        String action() default "";
        
        /**
         * A Tiles definition to forward to.  Mutually-exclusive with {@link #path}, {@link #navigateTo},
         * {@link #returnAction}, and {@link #action}.
         */
        String tilesDefinition() default "";
        
        /**
         * Tells whether the original query string will be restored on a rerun of a previous action.  Only valid when
         * <code>navigateTo</code> is <code>NavigateTo.previousAction</code>.
         * @return boolean
         */ 
        boolean restoreQueryString() default false;
        
        Class outputFormBeanType() default Void.class;
        String outputFormBean() default "";
        boolean redirect() default false; // optional
        boolean externalRedirect() default false; // optional
        ActionOutput[] actionOutputs() default {};
    }

    /**
     * page input
     */
    @Target( ANNOTATION_TYPE )
    @Retention( RUNTIME )
    public @interface ActionOutput
    {
        String name();
        Class type();
        String typeHint() default "";
        boolean required() default true;
        /** @deprecated Use {@link #required}. **/
        boolean nullable() default false;
    }

    @Target( ANNOTATION_TYPE )
    @Retention( RUNTIME )
    public @interface MessageBundle
    {
        String bundlePath(); // required - no default
        String bundleName() default ""; // optional
    }
    
    public enum NavigateTo
    {
        currentPage,
        previousPage,
        previousAction,
        page
    }

    /**
     * Validation rule: Required.
     */
    @Target( ANNOTATION_TYPE )
    @Retention( RUNTIME )
    public @interface ValidateRequired
    {
        boolean enabled() default true;
        String message() default "";
        String messageKey() default "";
        String arg0() default "";
        String arg0Key() default "";
        String arg1() default "";
        String arg1Key() default "";
        String arg2() default "";
        String arg2Key() default "";
        String arg3() default "";
        String arg3Key() default "";
    }
    
    /**
     * Validation rule: MinLength.
     */
    @Target( ANNOTATION_TYPE )
    @Retention( RUNTIME )
    public @interface ValidateMinLength
    {
        boolean enabled() default true;
        int chars(); // required
        String message() default "";
        String messageKey() default "";
        String arg0() default "";
        String arg0Key() default "";
        String arg1() default "";
        String arg1Key() default "";
        String arg2() default "";
        String arg2Key() default "";
        String arg3() default "";
        String arg3Key() default "";
    }

    /**
     * Validation rule: MaxLength.
     */
    @Target( ANNOTATION_TYPE )
    @Retention( RUNTIME )
    public @interface ValidateMaxLength
    {
        boolean enabled() default true;
        int chars(); // required
        String message() default "";
        String messageKey() default "";
        String arg0() default "";
        String arg0Key() default "";
        String arg1() default "";
        String arg1Key() default "";
        String arg2() default "";
        String arg2Key() default "";
        String arg3() default "";
        String arg3Key() default "";
    }

    /**
     * Validation rule: Mask.
     */
    @Target( ANNOTATION_TYPE )
    @Retention( RUNTIME )
    public @interface ValidateMask
    {
        boolean enabled() default true;
        String regex(); // required
        String message() default "";
        String messageKey() default "";
        String arg0() default "";
        String arg0Key() default "";
        String arg1() default "";
        String arg1Key() default "";
        String arg2() default "";
        String arg2Key() default "";
        String arg3() default "";
        String arg3Key() default "";
    }

    /**
     * Validation rule: Byte.
     */
    @Target( ANNOTATION_TYPE )
    @Retention( RUNTIME )
    public @interface ValidateType
    {
        boolean enabled() default true;
        Class type();
        String message() default "";
        String messageKey() default "";
        String arg0() default "";
        String arg0Key() default "";
        String arg1() default "";
        String arg1Key() default "";
        String arg2() default "";
        String arg2Key() default "";
        String arg3() default "";
        String arg3Key() default "";
    }

    /**
     * Validation rule: Date.
     */
    @Target( ANNOTATION_TYPE )
    @Retention( RUNTIME )
    public @interface ValidateDate
    {
        boolean enabled() default true;
        String pattern(); // required
        boolean strict() default false;
        String message() default "";
        String messageKey() default "";
        String arg0() default "";
        String arg0Key() default "";
        String arg1() default "";
        String arg1Key() default "";
        String arg2() default "";
        String arg2Key() default "";
        String arg3() default "";
        String arg3Key() default "";
    }

    /**
     * Validation rule: Range.
     */
    @Target( ANNOTATION_TYPE )
    @Retention( RUNTIME )
    public @interface ValidateRange
    {
        boolean enabled() default true;
        long minInt() default 0;
        long maxInt() default -1;
        double minFloat() default 0;
        double maxFloat() default -1;
        String message() default "";
        String messageKey() default "";
        String arg0() default "";
        String arg0Key() default "";
        String arg1() default "";
        String arg1Key() default "";
        String arg2() default "";
        String arg2Key() default "";
        String arg3() default "";
        String arg3Key() default "";
    }

    /**
     * Validation rule: CreditCard.
     */
    @Target( ANNOTATION_TYPE )
    @Retention( RUNTIME )
    public @interface ValidateCreditCard
    {
        boolean enabled() default true;
        String message() default "";
        String messageKey() default "";
        String arg0() default "";
        String arg0Key() default "";
        String arg1() default "";
        String arg1Key() default "";
        String arg2() default "";
        String arg2Key() default "";
        String arg3() default "";
        String arg3Key() default "";
    }

    /**
     * Validation rule: Email.
     */
    @Target( ANNOTATION_TYPE )
    @Retention( RUNTIME )
    public @interface ValidateEmail
    {
        boolean enabled() default true;
        String message() default "";
        String messageKey() default "";
        String arg0() default "";
        String arg0Key() default "";
        String arg1() default "";
        String arg1Key() default "";
        String arg2() default "";
        String arg2Key() default "";
        String arg3() default "";
        String arg3Key() default "";
    }

    /**
     * Validation rule: ValidWhen.
     */
    @Target( ANNOTATION_TYPE )
    @Retention( RUNTIME )
    public @interface ValidateValidWhen
    {
        boolean enabled() default true;
        String condition(); // required
        String message() default "";
        String messageKey() default "";
        String arg0() default "";
        String arg0Key() default "";
        String arg1() default "";
        String arg1Key() default "";
        String arg2() default "";
        String arg2Key() default "";
        String arg3() default "";
        String arg3Key() default "";
    }

    @Target( ANNOTATION_TYPE )
    @Retention( RUNTIME )
    public @interface ValidateCustomVariable
    {
        String name();
        String value();
    }
    
    /**
     * Validation rule: Custom.
     */
    @Target( ANNOTATION_TYPE )
    @Retention( RUNTIME )
    public @interface ValidateCustomRule
    {
        String rule();  // required
        ValidateCustomVariable[] variables() default {};
        String message() default "";
        String messageKey() default "";
        String arg0() default "";
        String arg0Key() default "";
        String arg1() default "";
        String arg1Key() default "";
        String arg2() default "";
        String arg2Key() default "";
        String arg3() default "";
        String arg3Key() default "";
    }

    /**
     * List of field validation rules.  Can be present on a bean property (method or field),
     * or within a {@link ValidatableProperty}.
     */
    @Target( ANNOTATION_TYPE )
    @Retention( RUNTIME )
    public @interface ValidationLocaleRules
    {
        ValidateRequired validateRequired() default @ValidateRequired(enabled=false);
        ValidateMinLength validateMinLength() default @ValidateMinLength(enabled=false, chars=-1);
        ValidateMaxLength validateMaxLength() default @ValidateMaxLength(enabled=false, chars=-1);
        ValidateMask validateMask() default @ValidateMask(enabled=false, regex="");
        ValidateType validateType() default @ValidateType(enabled=false, type=void.class);
        ValidateDate validateDate() default @ValidateDate(enabled=false, pattern="");
        ValidateRange validateRange() default @ValidateRange(enabled=false);
        ValidateCreditCard validateCreditCard() default @ValidateCreditCard(enabled=false);
        ValidateEmail validateEmail() default @ValidateEmail(enabled=false);
        ValidateValidWhen validateValidWhen() default @ValidateValidWhen(enabled=false, condition="");
        ValidateCustomRule[] validateCustomRules() default {};
        
        String language() default "";
        String country() default "";
        String variant() default "";
        boolean applyToUnhandledLocales() default false;
    }

    /**
     * Validation rules associated with particular bean fields. Valid within {@link ValidatableBean} or {@link Action}.
     */
    @Target( { ANNOTATION_TYPE, METHOD } )
    @Retention( RUNTIME )
    public @interface ValidatableProperty
    {
        String propertyName() default "";
        String displayName() default "";
        String displayNameKey() default "";
        ValidateRequired validateRequired() default @ValidateRequired(enabled=false);
        ValidateMinLength validateMinLength() default @ValidateMinLength(enabled=false, chars=-1);
        ValidateMaxLength validateMaxLength() default @ValidateMaxLength(enabled=false, chars=-1);
        ValidateMask validateMask() default @ValidateMask(enabled=false, regex="");
        ValidateType validateType() default @ValidateType(enabled=false, type=void.class);
        ValidateDate validateDate() default @ValidateDate(enabled=false, pattern="");
        ValidateRange validateRange() default @ValidateRange(enabled=false);
        ValidateCreditCard validateCreditCard() default @ValidateCreditCard(enabled=false);
        ValidateEmail validateEmail() default @ValidateEmail(enabled=false);
        ValidateValidWhen validateValidWhen() default @ValidateValidWhen(enabled=false, condition="");
        ValidateCustomRule[] validateCustomRules() default {};
        ValidationLocaleRules[] localeRules() default {};
    }
    
    /**
     * Validation fields (and associated rules) associated with a bean type.  Valid within
     * {@link Controller}.
     */
    @Target( ANNOTATION_TYPE )
    @Retention( RUNTIME )
    public @interface ValidatableBean
    {
        Class type();                                    // required
        ValidatableProperty[] validatableProperties();   // required
    }
    
    @Target( TYPE )
    @Retention( RUNTIME )
    public @interface FormBean
    {
        String messageBundle() default "";
    }

    @Target( FIELD )
    @Retention( RUNTIME )
    public @interface SharedFlowField
    {
        /**
         * The name of the shared flow, as declared in {@link SharedFlowRef#name()} 
         */ 
        String name();
    }

    @Target( FIELD )
    @Retention( RUNTIME )
    public @interface PageFlowField
    {
    }
    
    @Target( TYPE )
            @Retention( RUNTIME )
            public @interface FacesBacking
    {
    }
    
    @Target( METHOD )
    @Retention( RUNTIME )
    public @interface CommandHandler
    {
        RaiseAction[] raiseActions() default {};
    }
    
    @Target( ANNOTATION_TYPE )
    @Retention( RUNTIME )
    public @interface RaiseAction
    {
        String action();
        String outputFormBean() default "";
    }
    
    @Target( ANNOTATION_TYPE )
    @Retention( RUNTIME )
    public @interface SharedFlowRef
    {
        Class type();
        String name();
    }
}
