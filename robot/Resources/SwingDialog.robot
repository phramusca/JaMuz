*** Settings ***
Library     RemoteSwingLibrary

*** Keywords ***
Set Folder In FileChooser
    [Arguments]    ${DialogTitle}    ${TextValue}
    Select Dialog    ${DialogTitle}
    List Components in Context
    Select Context    2 
    List Components in Context
    Select Context    20
    List Components in Context
    Select Context    0
    List Components in Context
    Insert Into Text Field   0   ${TextValue}
    Select Dialog    ${DialogTitle}
    Push Button    SynthFileChooser.approveButton

Set Location in Options
    [Arguments]    ${DialogTitle}    ${TextValue}    ${ButtonName}
    Select Dialog    DialogOptions
    Select Tab Pane    DialogOptionsTabbedPane
    Select Tab As Context    Check Locations
    Push Button    ${ButtonName}
    Set Folder In FileChooser    ${DialogTitle}    ${TextValue}