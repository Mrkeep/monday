package com.hyb.backstage.base;

public interface IValidator<T> {
    /**
     * 验证前端提交参数合法性
     *
     * @param t
     * @return
     * @throws ValidateFailedException
     */
    boolean validate(T t) throws ValidateFailedException;
}
