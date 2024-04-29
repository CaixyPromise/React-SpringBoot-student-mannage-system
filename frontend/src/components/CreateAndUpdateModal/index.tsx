import UpdateModal from "@/pages/Admin/User/components/UpdateModal";
import CreateModal from "@/pages/Admin/User/components/CreateModal";
import {ActionType, ProColumns} from "@ant-design/pro-components";
import React from "react";

interface ModalConfig {
    visible: boolean;
    columns: ProColumns<any>[];
    onSubmit: (values: any) => Promise<BaseResponse<any>>;
    successCallback?: (returnValue: any) => void;
    onCancel: () => void;
}

const generateOperationModal = ({
    createConfig,
    updateConfig,
    currentRow,
    actionRef,
}: {
    createConfig: ModalConfig;
    updateConfig: ModalConfig;
    currentRow?: API.User; // 可选参数，用于更新表单时的默认值
    actionRef: React.Ref<ActionType>;
}) => {
    return (
        <>
            <CreateModal
                visible={createConfig.visible}
                columns={createConfig.columns}
                onSubmit={async (value) => {
                    const response = await createConfig.onSubmit(value);
                    // if (createConfig.successCallback) createConfig.successCallback(response);
                    actionRef.current?.reload();
                }}
                onCancel={createConfig.onCancel}
            />
            <UpdateModal
                visible={updateConfig.visible}
                columns={updateConfig.columns}
                defaultValue={currentRow}
                onSubmit={async (value) => {
                    const response = await updateConfig.onSubmit(value);
                    // if (updateConfig.successCallback) updateConfig.successCallback(response);
                    actionRef.current?.reload();
                }}
                onCancel={updateConfig.onCancel}
            />
        </>
    );
};
