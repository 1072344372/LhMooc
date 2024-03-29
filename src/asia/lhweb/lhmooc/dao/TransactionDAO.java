package asia.lhweb.lhmooc.dao;

import asia.lhweb.lhmooc.model.bean.Transaction;

/**
 * 交易流水服务
 *
 * @author Administrator
 * @description 针对表【transaction(交易流水表)】的数据库操作Service
 * @createDate 2024-03-11 21:23:21
 * @date 2024/03/11
 */
public interface TransactionDAO {

    /**
     * 保存
     *
     * @param transaction 事务
     * @return int
     */
    int save(Transaction transaction);
}
