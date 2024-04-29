
import {ActionType, ProColumns, ProTable} from '@ant-design/pro-components';
import '@umijs/max';
import {message, Modal} from 'antd';
import React, {useRef} from 'react';

interface UpdateProps
{
    defaultValue?: any;
    visible: boolean;
    columns: ProColumns<any>[];
    onSubmit:  (values?: any, id?: any) => Promise<BaseResponse<any>>;
    successCallback?: (returnValue: any) => void;
    onCancel: () => void;
}


/**
 * 更新弹窗
 * @param props
 * @constructor
 */
const UpdateModal: React.FC<UpdateProps> = ({
    defaultValue,
    visible,
    columns,
    onSubmit,
    successCallback,
    onCancel
}: UpdateProps) =>
{
    if (!defaultValue)
    {
        return <></>;
    }

    return (
        <Modal
            destroyOnClose
            title={'更新'}
            open={visible}
            footer={null}
            onCancel={() =>
            {
                onCancel?.();
            }}
        >
            <ProTable
                key={defaultValue?.id || 'defaultKey'}
                type="form"
                columns={columns}
                form={{
                    initialValues: defaultValue,
                }}
                onSubmit={async (values: any) =>
                {
                    const hide = message.loading('正在提交')
                    const {code, data} = await onSubmit({
                        ...defaultValue,
                        ...values,
                        id: defaultValue.id as any,
                    })
                    if (code === 0)
                    {
                        successCallback?.(data);
                    }
                    hide();
                }}
            />
        </Modal>
    );
};
export default UpdateModal;
