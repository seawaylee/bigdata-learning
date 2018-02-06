package annotation.router;

import annotation.router.annotation.Md5RangeSelector;
import annotation.router.common.Md5RangeRouter;
import annotation.router.service.GroupAService;
import annotation.router.service.GroupBService;
import annotation.router.service.IGroupService;

/**
 * @author NikoBelic
 * @create 2018/2/6 15:46
 */
public class GroupServiceRouter extends Md5RangeRouter<IGroupService> {

    @Md5RangeSelector(start = '0', end = '9')
    private GroupAService groupAService = new GroupAService();

    @Md5RangeSelector(start = 'a', end = 'f')
    private GroupBService groupBService = new GroupBService();

    public GroupServiceRouter() throws IllegalAccessException {
        super.init();
    }
}
