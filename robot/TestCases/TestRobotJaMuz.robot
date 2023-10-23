*** Settings ***
Library     RemoteSwingLibrary
Resource    ../Resources/SwingDialog.robot

*** Test Cases ***
My Test Case
    # Start Application    my_app    java -jar ~/actions-runner/_work/JaMuz/JaMuz/dist/JaMuz.jar
    Start Application    my_app    java -jar C:\\dist\\JaMuz.jar
    Select Window    PanelMain
    Select Tab Pane    PanelMainTabbedPane
    Select Tab As Context    Options
    Select Context    PanelOptionsMachines
    Push Button    PanelOptionsMachinesEditButton

    Select Dialog    DialogOptions
    Select Tab Pane    DialogOptionsTabbedPane
    Select Tab As Context    Machine and Library
    Check Check Box    DialogOptionsLibraryMasterCheckBox
    Push Button    DialogOptionsLibrarySelectButton
    Set Folder In FileChooser    Library Location    C:\\Users\\RaphaelCamus\\Documents\\Perso\\Music\\Archive

    Set Location in Options    New files location    C:\\Users\\RaphaelCamus\\Documents\\Perso\\Music\\New    DialogOptionsCheckLocationsSelectNew
    Set Location in Options    Location to move OK    C:\\Users\\RaphaelCamus\\Documents\\Perso\\Music\\OK    DialogOptionsCheckLocationsSelectOK  
    Set Location in Options    Location to move Manual    C:\\Users\\RaphaelCamus\\Documents\\Perso\\Music\\Manual    DialogOptionsCheckLocationsSelectManual  
    Set Location in Options    Location to move KO    C:\\Users\\RaphaelCamus\\Documents\\Perso\\Music\\KO    DialogOptionsCheckLocationsSelectKO
    
    Select Dialog    DialogOptions
    Push Button    DialogOptionsSaveButton

    Select Window    PanelMain
    Select Tab Pane    PanelMainTabbedPane
    Select Tab As Context    Check
    Select Context    PanelCheckProcess
    Push Button     PanelCheckScanLibraryButton
    