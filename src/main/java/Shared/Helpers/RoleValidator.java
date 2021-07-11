package Shared.Helpers;

import Shared.Models.Employee;
import Shared.Models.EmployeeEnum;
import Shared.Models.QueryTypes;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RoleValidator {
    private static final Map<EmployeeEnum,QueryTypes[]> rolesMapper =
            new HashMap<EmployeeEnum,QueryTypes[]>(){{
        put(EmployeeEnum.SUPERVISOR,new QueryTypes[]{
                QueryTypes.CREATE,
                QueryTypes.UPDATE,
                QueryTypes.DELETE,
                QueryTypes.SELECT});

        put(EmployeeEnum.SUBORDINATE,new QueryTypes[]{
                QueryTypes.SELECT});
    }};



    public static boolean isAuthorized(QueryTypes queryType, EmployeeEnum role){

        return Arrays.stream(rolesMapper.get(role)).filter(r -> r.equals(queryType)).count() != 0;
    }

}
