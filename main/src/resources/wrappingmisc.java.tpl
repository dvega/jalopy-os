public class WrappingMisc
{
    public void foo(int              param1,
                    String           param2,
                    com.foo.MyObject param3,
                    Object           param4)
        throws FooException,
               SillyException
    {
        String[] constraints =
        {
            "patternPanel.top=form.top", "patternPanel.hcenter=form.hcenter",
            "okButton.top=patternPanel.bottom+20",
            "okButton.right=form.hcenter-10", "cancelButton.vcenter=10",
            "cancelButton.left=10"
        };

        message.format(ERROR_SOURCE_ADDRESS).param(m_session.getAimName())
               .send();
        _userDatabase.addUser("Name", encryptPassword("password", _secretKey),
                              "123 fake address");

        String comma = spaceAfterComma ? COMMA_SPACE : COMMA;
LOOP:
        for (AST child = tree.getFirstChild();
             child != null;
             child = child.getNextSibling())
        {
            switch (child.getType())
            {
                case JavaTokenTypes.CLASS_DEF :

                    if ((condition1 && condition2) ||
                        (condition3 && condition4) ||
                        !(condition5 && condition6))
                    {
                        ;
                    }

                    break LOOP;

                default :
                    break LOOP;
            }
        }
    }
}