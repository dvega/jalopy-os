public class Wrapping
    extends AnotherFoo
    implements Fooable,
               Forgetable
{
    public void foo(int              param1,
                    String           param2,
                    com.foo.MyObject param3,
                    Object           param4)
        throws FooException,
               SillyException
    {
        message.format(ERROR_SOURCE_ADDRESS).param(m_session.getAimName())
               .send();
        _userDatabase.addUser("Name", encryptPassword("password", _secretKey),
                              "123 fake address");

        if ((condition1 && condition2) ||
            (condition3 && condition4) ||
            !(condition5 && condition6))
        {
            ;
        }

		this.customerNumber =
			new CustomerNumber(
				ServiceManager.createService()
				.createNewKey(
				    VerySpecialKeyCreationService.IMPORTANT_SERVICE_KEY
				)
			);

		ServiceManager.createService()
		.createNewKey(
		    VerySpecialKeyCreationService.IMPORTANT_SERVICE_KEY
		);

		do {
			;
		} while (
			method.isStatic()
			&& method.isPublic()
			&& method.isFinal()
			&& method.isSynchronized()
		);
    }
}