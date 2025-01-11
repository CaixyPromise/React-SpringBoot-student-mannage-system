import {addUserUsingPost1} from "@/services/backend/userController";
import {ProColumns, ProTable} from '@ant-design/pro-components';
import '@umijs/max';
import {message, Modal} from 'antd';
import React from 'react';

interface CreateProps
{
    visible: boolean;
    columns: ProColumns<any>[];
    onSubmit:  (values: any) => Promise<BaseResponse<any>>;
    successCallback?: (returnValue: any) => void;
    onCancel: () => void;
}

/**
 * 创建弹窗
 * @param props
 * @constructor
 */
const CreateModal: React.FC<CreateProps> = ({
    visible,
    columns,
    onSubmit,
    successCallback,
    onCancel
}: CreateProps) =>
{

    return (
        <Modal
            destroyOnClose
            title={'创建'}
            open={visible}
            footer={null}
            onCancel={() =>
            {
                onCancel?.();
            }}
        >
            <ProTable
                type="form"
                columns={columns}
                onSubmit={async (values: any) =>
                {
                    const hide = message.loading('正在提交')

                    const {code, data} = await onSubmit?.({
                        ...values,
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
export default CreateModal;
