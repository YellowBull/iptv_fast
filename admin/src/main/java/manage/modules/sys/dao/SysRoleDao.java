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
import manage.modules.sys.entity.SysRoleEntity;
import org.springframework.stereotype.Repository;

/**
 * 角色管理
 */
@Repository
public interface SysRoleDao extends BaseMapper<SysRoleEntity> {
	

}
