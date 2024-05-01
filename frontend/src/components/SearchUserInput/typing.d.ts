import React from "react";
import {UserValue} from "@/components/SearchUserInput/index";
import type {SelectProps} from "antd";

export interface SearchUserInputProps
{
    mode?: "multiple" | "tags";
    style?: React.CSSProperties; // Use React.CSSProperties for style objects
    placeholder?: string;
    fetchOptions: (text: string) => Promise<UserValue[]>;
    value: UserValue[];
    setValue: React.Dispatch<React.SetStateAction<UserValue[]>>;
    maxCount: number;
}

export interface DebounceSelectProps<ValueType = any>
    extends Omit<SelectProps<ValueType | ValueType[]>, 'options' | 'children'>
{
    fetchOptions: (search: string) => Promise<ValueType[]>;
    debounceTimeout?: number;
}

export interface UserValue
{
    label: string;
    value: string;
}
