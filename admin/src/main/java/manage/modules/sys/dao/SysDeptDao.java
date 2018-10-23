/**
 * Copyright 2018 人人开源 http://www.renren.io
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package manage.modules.sys.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import manage.modules.sys.entity.SysDeptEntity;
import manage.modules.sys.entity.SysRoleEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 部门管理
 */
@Repository
public interface SysDeptDao extends BaseMapper<SysDeptEntity> {

    /**
     * 查询子权限ID列表
     * @param parentId  上级权限ID 这里不用
     */
    List<Long> queryDetpIdList(Long parentId);

    List<Long> queryRoleId(Long deptId);

    List<String> queryRole(Long roleId);
}
