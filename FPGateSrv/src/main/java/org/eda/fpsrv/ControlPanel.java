/*
 * Copyright (C) 2015 EDA Ltd.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.eda.fpsrv;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.awt.AWTException;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.Properties;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;
import org.eda.cb.CBIOService;
import org.eda.cb.CBIOService.ConnectionState;
import org.eda.fdevice.FPPrinterPool;
import org.restlet.data.LocalReference;
import org.restlet.data.MediaType;
import org.restlet.ext.crypto.DigestUtils;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

/**
 *
 * @author Dimitar Angelov
 */
public class ControlPanel extends javax.swing.JFrame {

    private static int logViewCapacity = 300; // lines
    private static int logViewCapacityDelta = 30; // lines
    private Logger ll;
    /**
     * Creates new form ControlPanel
     */
    public ControlPanel() {
//        this.setLayout(new BorderLayout());        
        initComponents();
        jlAppVersion.setText("Версия: "+FPServer.application.getVersion());
        jlBuildInfo.setText("Билд: "+FPServer.application.getBuildNumber());
        LogHandler lh = new LogHandler(this);
        FPServer.addLogHandler(lh);
        // Catch System out
        System.setOut(new SysOutInterceptor(System.out, this));
        System.setErr(new SysOutInterceptor(System.err, this));
        System.out.print("Control Panel attached to system out.");
        System.err.print("Control Panel attached to system err.");
        jtaLog.append("Application base path:"+FPServer.application.getAppBasePath()+ "\n");
        hideToSystemTray();
    }
    
    private void adjustTextAreaToCapacity(javax.swing.JTextArea ta) {
        int lines = ta.getLineCount();
        if (lines > ControlPanel.logViewCapacity+ControlPanel.logViewCapacityDelta) {
            try {
                int lineOffset = ta.getLineEndOffset(lines-ControlPanel.logViewCapacity);
                ta.replaceRange("", 0, lineOffset);
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }
        }    
    }
    
    private class LogHandler extends Handler {
        private ControlPanel panel;

        public LogHandler() {}

        public LogHandler(ControlPanel panel) {
            this.panel = panel;
        }

        @Override
        public void publish(LogRecord lr) {
            String threadId = Long.toString(Thread.currentThread().getId());
            String msg = threadId+":"+lr.getMessage();

            panel.jtaLog.append(msg + "\n");
            panel.adjustTextAreaToCapacity(panel.jtaLog);
            panel.jtaLog.setCaretPosition(panel.jtaLog.getDocument().getLength());
            
            panel.adjustStartStopControls();
        }

        @Override
        public void flush() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void close() throws SecurityException {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
    
    private class SysOutInterceptor extends PrintStream {
        private final ControlPanel panel;
        
        public SysOutInterceptor(OutputStream out, ControlPanel panel) {
            super(out, true);
            this.panel = panel;
        }

        @Override
        public void print(String s) {
            // TODO: Remove this ugly patch when ormlite will be fixed!
            if (!s.contains("you seem to not be using the Xerial SQLite driver")) {
                panel.jtaSysOut.append(s + "\n");
                panel.adjustTextAreaToCapacity(panel.jtaSysOut);
                panel.jtaSysOut.setCaretPosition(panel.jtaSysOut.getDocument().getLength());
            }    
            super.print(s);
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jlAppName = new javax.swing.JLabel();
        jlAppVersion = new javax.swing.JLabel();
        jlBuildInfo = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jbStart = new javax.swing.JButton();
        jbStop = new javax.swing.JButton();
        jtbMain = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtaLog = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtaSysOut = new javax.swing.JTextArea();
        jpSettings = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jtServerPort = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jtAdminName = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jcbDisableAnonymousPrint = new javax.swing.JCheckBox();
        jSeparator2 = new javax.swing.JSeparator();
        jbSave = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();
        jtAdminPassword = new javax.swing.JPasswordField();
        jtAdminPassword2 = new javax.swing.JPasswordField();
        jtServerAddr = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jlGoAdmin = new javax.swing.JLabel();
        jcbUseSSL = new javax.swing.JCheckBox();
        jtServerPortSSL = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jComboBoxLLProtocol = new javax.swing.JComboBox<>();
        jComboBoxLLDevice = new javax.swing.JComboBox<>();
        jComboBoxLLFPCBase = new javax.swing.JComboBox<>();
        jcbStartMinimized = new javax.swing.JCheckBox();
        jcbCheckVersionAtStartup = new javax.swing.JCheckBox();
        jbCheckVersion = new javax.swing.JButton();
        jcbPoolEnable = new javax.swing.JCheckBox();
        jtPoolDeadtime = new javax.swing.JTextField();
        jlPoolDeadtime = new javax.swing.JLabel();
        jbClearPool = new javax.swing.JButton();
        jcbZFPLabServerAutoStart = new javax.swing.JCheckBox();
        jbZFPStart = new javax.swing.JButton();
        jbZFPStop = new javax.swing.JButton();
        jcbCoAutoStart = new javax.swing.JCheckBox();
        jbCBIOStart = new javax.swing.JButton();
        jbCBIOStop = new javax.swing.JButton();
        jlCBIOLogLevel = new javax.swing.JLabel();
        jComboBoxLLCBIO = new javax.swing.JComboBox<>();
        jSeparator4 = new javax.swing.JSeparator();
        jcbUseHTTP = new javax.swing.JCheckBox();
        jtbCoERP = new javax.swing.JTabbedPane();
        jpCoERP = new javax.swing.JPanel();
        jlCoURL = new javax.swing.JLabel();
        jtCoURL = new javax.swing.JTextField();
        jlCoUser = new javax.swing.JLabel();
        jtCoUser = new javax.swing.JTextField();
        jlCoPass = new javax.swing.JLabel();
        jtCoPass = new javax.swing.JPasswordField();
        jlCBIOServiceState = new javax.swing.JLabel();
        jtCBIOServiceState = new javax.swing.JTextField();
        jpCoERP1 = new javax.swing.JPanel();
        jlCoURL1 = new javax.swing.JLabel();
        jtCoURL1 = new javax.swing.JTextField();
        jlCoUser1 = new javax.swing.JLabel();
        jtCoUser1 = new javax.swing.JTextField();
        jlCoPass1 = new javax.swing.JLabel();
        jtCoPass1 = new javax.swing.JPasswordField();
        jlCBIOServiceState1 = new javax.swing.JLabel();
        jtCBIOServiceState1 = new javax.swing.JTextField();
        jpCoERP2 = new javax.swing.JPanel();
        jlCoURL2 = new javax.swing.JLabel();
        jtCoURL2 = new javax.swing.JTextField();
        jlCoUser2 = new javax.swing.JLabel();
        jtCoUser2 = new javax.swing.JTextField();
        jlCoPass2 = new javax.swing.JLabel();
        jtCoPass2 = new javax.swing.JPasswordField();
        jlCBIOServiceState2 = new javax.swing.JLabel();
        jtCBIOServiceState2 = new javax.swing.JTextField();
        jpCoERP3 = new javax.swing.JPanel();
        jlCoURL3 = new javax.swing.JLabel();
        jtCoURL3 = new javax.swing.JTextField();
        jlCoUser3 = new javax.swing.JLabel();
        jtCoUser3 = new javax.swing.JTextField();
        jlCoPass3 = new javax.swing.JLabel();
        jtCoPass3 = new javax.swing.JPasswordField();
        jlCBIOServiceState3 = new javax.swing.JLabel();
        jtCBIOServiceState3 = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        jtaLicense = new javax.swing.JTextArea();
        jlAStatus = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("EDA FPGate Server");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jlAppName.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jlAppName.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/logo_eda_s.gif")));
        jlAppName.setText("EDA Fiscal Printer Gateway");
        jlAppName.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jlAppNameMouseClicked(evt);
            }
        });

        jlAppVersion.setText("Версия: 1.0.0");
        jlAppVersion.setToolTipText("");

        jlBuildInfo.setForeground(new java.awt.Color(153, 153, 153));
        jlBuildInfo.setText("Билд ...");

        jbStart.setText("Старт");
        jbStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbStartActionPerformed(evt);
            }
        });

        jbStop.setText("Стоп");
        jbStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbStopActionPerformed(evt);
            }
        });

        jtbMain.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtbMainFocusGained(evt);
            }
        });

        jtaLog.setEditable(false);
        jtaLog.setBackground(new java.awt.Color(0, 0, 0));
        jtaLog.setColumns(20);
        jtaLog.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        jtaLog.setForeground(new java.awt.Color(204, 204, 204));
        jtaLog.setRows(5);
        jtaLog.setText("FP Gateway");
        jScrollPane1.setViewportView(jtaLog);

        jtbMain.addTab("Лог на достъпа", jScrollPane1);

        jtaSysOut.setEditable(false);
        jtaSysOut.setBackground(new java.awt.Color(0, 0, 0));
        jtaSysOut.setColumns(20);
        jtaSysOut.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        jtaSysOut.setForeground(new java.awt.Color(204, 204, 204));
        jtaSysOut.setLineWrap(true);
        jtaSysOut.setRows(5);
        jScrollPane2.setViewportView(jtaSysOut);

        jtbMain.addTab("Системен лог", jScrollPane2);

        jLabel1.setLabelFor(jtServerPort);
        jLabel1.setText("Сървър порт");

        jtServerPort.setText("8182");
        jtServerPort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtServerPortActionPerformed(evt);
            }
        });

        jLabel2.setLabelFor(jtAdminName);
        jLabel2.setText("Администратор име");

        jtAdminName.setText("fpgadmin");
        jtAdminName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtAdminNameActionPerformed(evt);
            }
        });

        jLabel3.setLabelFor(jtAdminPassword);
        jLabel3.setText("Администратор парола");

        jLabel4.setLabelFor(jtAdminPassword2);
        jLabel4.setText("Потвърждение на парола");

        jcbDisableAnonymousPrint.setText("Забрана на анонимен печат");

        jSeparator2.setToolTipText("");

        jbSave.setText("Запис");
        jbSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbSaveActionPerformed(evt);
            }
        });

        jbCancel.setText("Отказ");
        jbCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbCancelActionPerformed(evt);
            }
        });

        jtServerAddr.setText("127.0.0.1");
        jtServerAddr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtServerAddrActionPerformed(evt);
            }
        });

        jLabel5.setLabelFor(jtServerAddr);
        jLabel5.setText("Сървър адрес");
        jLabel5.setToolTipText("Leave empty to bind on all addresses.");

        jlGoAdmin.setForeground(new java.awt.Color(51, 102, 255));
        jlGoAdmin.setText("Go to Admin");
        jlGoAdmin.setToolTipText("Open administration panel");
        jlGoAdmin.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jlGoAdmin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jlGoAdminMouseClicked(evt);
            }
        });

        jcbUseSSL.setText("HTTPS");
        jcbUseSSL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbUseSSLActionPerformed(evt);
            }
        });

        jtServerPortSSL.setText("8183");

        jLabel6.setLabelFor(jComboBoxLLProtocol);
        jLabel6.setText("Лог ниво - протокол");

        jLabel7.setLabelFor(jComboBoxLLDevice);
        jLabel7.setText("Лог ниво - устройство ниско");

        jLabel8.setLabelFor(jComboBoxLLFPCBase);
        jLabel8.setText("Лог ниво - устройство приложно");

        jComboBoxLLProtocol.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jComboBoxLLDevice.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jComboBoxLLFPCBase.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jcbStartMinimized.setText("Стартиране на минимизиран прозорец");
        jcbStartMinimized.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbStartMinimizedActionPerformed(evt);
            }
        });

        jcbCheckVersionAtStartup.setText("Проверка за нови версии при стартиране");

        jbCheckVersion.setText("Провери сега");
        jbCheckVersion.setToolTipText("");
        jbCheckVersion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbCheckVersionActionPerformed(evt);
            }
        });

        jcbPoolEnable.setText("Разрешаван на постоянен пул от принтери");

        jtPoolDeadtime.setText("5");
        jtPoolDeadtime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtPoolDeadtimeActionPerformed(evt);
            }
        });

        jlPoolDeadtime.setLabelFor(jtPoolDeadtime);
        jlPoolDeadtime.setText("Време за престой в пула, мин");

        jbClearPool.setText("Изчистване на пула");
        jbClearPool.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbClearPoolActionPerformed(evt);
            }
        });

        jcbZFPLabServerAutoStart.setText("ZFPLabServer автоматичен старт");
        jcbZFPLabServerAutoStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbZFPLabServerAutoStartActionPerformed(evt);
            }
        });

        jbZFPStart.setText("Старт");
        jbZFPStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbZFPStartActionPerformed(evt);
            }
        });

        jbZFPStop.setText("Стоп");
        jbZFPStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbZFPStopActionPerformed(evt);
            }
        });

        jcbCoAutoStart.setText("Автоматичен старт");

        jbCBIOStart.setText("Старт");
        jbCBIOStart.setToolTipText("");
        jbCBIOStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbCBIOStartActionPerformed(evt);
            }
        });

        jbCBIOStop.setText("Стоп");
        jbCBIOStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbCBIOStopActionPerformed(evt);
            }
        });

        jlCBIOLogLevel.setText("Лог ниво - Колибри ERP");

        jComboBoxLLCBIO.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jcbUseHTTP.setText("HTTP");

        jlCoURL.setText("WS URL");

        jlCoUser.setText("Потребител");

        jlCoPass.setText("Парола");

        jlCBIOServiceState.setText("Статус");

        jtCBIOServiceState.setEditable(false);

        javax.swing.GroupLayout jpCoERPLayout = new javax.swing.GroupLayout(jpCoERP);
        jpCoERP.setLayout(jpCoERPLayout);
        jpCoERPLayout.setHorizontalGroup(
            jpCoERPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpCoERPLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpCoERPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlCoURL)
                    .addComponent(jlCoUser))
                .addGap(18, 18, 18)
                .addGroup(jpCoERPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jpCoERPLayout.createSequentialGroup()
                        .addComponent(jtCoUser, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(45, 45, 45)
                        .addComponent(jlCoPass)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jtCoPass, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jlCBIOServiceState)
                        .addGap(18, 18, 18)
                        .addComponent(jtCBIOServiceState, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jtCoURL, javax.swing.GroupLayout.PREFERRED_SIZE, 596, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jpCoERPLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jtCoPass, jtCoUser});

        jpCoERPLayout.setVerticalGroup(
            jpCoERPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpCoERPLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jpCoERPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlCoURL)
                    .addComponent(jtCoURL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpCoERPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpCoERPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jlCBIOServiceState)
                        .addComponent(jtCBIOServiceState, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jpCoERPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jlCoPass)
                        .addComponent(jtCoPass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jtCoUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jlCoUser))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jtbCoERP.addTab("Колибри ERP 1", jpCoERP);

        jlCoURL1.setText("WS URL");

        jlCoUser1.setText("Потребител");

        jlCoPass1.setText("Парола");

        jlCBIOServiceState1.setText("Статус");

        jtCBIOServiceState1.setEditable(false);

        javax.swing.GroupLayout jpCoERP1Layout = new javax.swing.GroupLayout(jpCoERP1);
        jpCoERP1.setLayout(jpCoERP1Layout);
        jpCoERP1Layout.setHorizontalGroup(
            jpCoERP1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpCoERP1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpCoERP1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlCoURL1)
                    .addComponent(jlCoUser1))
                .addGap(18, 18, 18)
                .addGroup(jpCoERP1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jpCoERP1Layout.createSequentialGroup()
                        .addComponent(jtCoUser1, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(45, 45, 45)
                        .addComponent(jlCoPass1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jtCoPass1, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jlCBIOServiceState1)
                        .addGap(18, 18, 18)
                        .addComponent(jtCBIOServiceState1, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jtCoURL1, javax.swing.GroupLayout.PREFERRED_SIZE, 596, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jpCoERP1Layout.setVerticalGroup(
            jpCoERP1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpCoERP1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jpCoERP1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlCoURL1)
                    .addComponent(jtCoURL1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpCoERP1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpCoERP1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jlCBIOServiceState1)
                        .addComponent(jtCBIOServiceState1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jpCoERP1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jlCoPass1)
                        .addComponent(jtCoPass1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jtCoUser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jlCoUser1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jtbCoERP.addTab("Колибри ERP 2", jpCoERP1);

        jlCoURL2.setText("WS URL");

        jlCoUser2.setText("Потребител");

        jlCoPass2.setText("Парола");

        jlCBIOServiceState2.setText("Статус");

        jtCBIOServiceState2.setEditable(false);

        javax.swing.GroupLayout jpCoERP2Layout = new javax.swing.GroupLayout(jpCoERP2);
        jpCoERP2.setLayout(jpCoERP2Layout);
        jpCoERP2Layout.setHorizontalGroup(
            jpCoERP2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpCoERP2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpCoERP2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlCoURL2)
                    .addComponent(jlCoUser2))
                .addGap(18, 18, 18)
                .addGroup(jpCoERP2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jpCoERP2Layout.createSequentialGroup()
                        .addComponent(jtCoUser2, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(45, 45, 45)
                        .addComponent(jlCoPass2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jtCoPass2, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jlCBIOServiceState2)
                        .addGap(18, 18, 18)
                        .addComponent(jtCBIOServiceState2, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jtCoURL2, javax.swing.GroupLayout.PREFERRED_SIZE, 596, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jpCoERP2Layout.setVerticalGroup(
            jpCoERP2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpCoERP2Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jpCoERP2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlCoURL2)
                    .addComponent(jtCoURL2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpCoERP2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpCoERP2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jlCBIOServiceState2)
                        .addComponent(jtCBIOServiceState2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jpCoERP2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jlCoPass2)
                        .addComponent(jtCoPass2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jtCoUser2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jlCoUser2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jtbCoERP.addTab("Колибри ERP 3", jpCoERP2);

        jlCoURL3.setText("WS URL");

        jlCoUser3.setText("Потребител");

        jlCoPass3.setText("Парола");

        jlCBIOServiceState3.setText("Статус");

        jtCBIOServiceState3.setEditable(false);

        javax.swing.GroupLayout jpCoERP3Layout = new javax.swing.GroupLayout(jpCoERP3);
        jpCoERP3.setLayout(jpCoERP3Layout);
        jpCoERP3Layout.setHorizontalGroup(
            jpCoERP3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpCoERP3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpCoERP3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlCoURL3)
                    .addComponent(jlCoUser3))
                .addGap(18, 18, 18)
                .addGroup(jpCoERP3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jpCoERP3Layout.createSequentialGroup()
                        .addComponent(jtCoUser3, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(45, 45, 45)
                        .addComponent(jlCoPass3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jtCoPass3, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jlCBIOServiceState3)
                        .addGap(18, 18, 18)
                        .addComponent(jtCBIOServiceState3, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jtCoURL3, javax.swing.GroupLayout.PREFERRED_SIZE, 596, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jpCoERP3Layout.setVerticalGroup(
            jpCoERP3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpCoERP3Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jpCoERP3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlCoURL3)
                    .addComponent(jtCoURL3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpCoERP3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpCoERP3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jlCBIOServiceState3)
                        .addComponent(jtCBIOServiceState3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jpCoERP3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jlCoPass3)
                        .addComponent(jtCoPass3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jtCoUser3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jlCoUser3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jtbCoERP.addTab("Колибри ERP 4", jpCoERP3);

        javax.swing.GroupLayout jpSettingsLayout = new javax.swing.GroupLayout(jpSettings);
        jpSettings.setLayout(jpSettingsLayout);
        jpSettingsLayout.setHorizontalGroup(
            jpSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpSettingsLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jbSave)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jbCancel)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpSettingsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpSettingsLayout.createSequentialGroup()
                        .addGroup(jpSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jpSettingsLayout.createSequentialGroup()
                                .addGroup(jpSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel5))
                                .addGap(34, 34, 34)
                                .addGroup(jpSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jpSettingsLayout.createSequentialGroup()
                                        .addGroup(jpSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jtServerAddr)
                                            .addComponent(jtAdminName, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)
                                            .addComponent(jtAdminPassword))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(jpSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jcbDisableAnonymousPrint)
                                            .addGroup(jpSettingsLayout.createSequentialGroup()
                                                .addGap(3, 3, 3)
                                                .addComponent(jLabel4)
                                                .addGap(18, 18, 18)
                                                .addComponent(jtAdminPassword2, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jpSettingsLayout.createSequentialGroup()
                                                .addComponent(jcbCheckVersionAtStartup)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jbCheckVersion)))
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addGroup(jpSettingsLayout.createSequentialGroup()
                                        .addComponent(jcbUseHTTP)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jtServerPort, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jcbUseSSL)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jtServerPortSSL, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jcbStartMinimized)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jlGoAdmin))))
                            .addComponent(jlCBIOLogLevel)
                            .addGroup(jpSettingsLayout.createSequentialGroup()
                                .addGroup(jpSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jpSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jComboBoxLLProtocol, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jpSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jComboBoxLLCBIO, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jpSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(jComboBoxLLFPCBase, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jComboBoxLLDevice, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jpSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jcbPoolEnable)
                                    .addGroup(jpSettingsLayout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(jlPoolDeadtime)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jtPoolDeadtime, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jbClearPool))
                                    .addGroup(jpSettingsLayout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jcbZFPLabServerAutoStart)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jbZFPStart)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jbZFPStop)))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(6, 6, 6))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpSettingsLayout.createSequentialGroup()
                        .addGroup(jpSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpSettingsLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jcbCoAutoStart)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jbCBIOStart)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jbCBIOStop))
                            .addComponent(jSeparator2)
                            .addGroup(jpSettingsLayout.createSequentialGroup()
                                .addComponent(jtbCoERP, javax.swing.GroupLayout.PREFERRED_SIZE, 758, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jSeparator4, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addContainerGap())))
        );
        jpSettingsLayout.setVerticalGroup(
            jpSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpSettingsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jtServerPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlGoAdmin)
                    .addComponent(jcbUseSSL)
                    .addComponent(jtServerPortSSL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcbUseHTTP)
                    .addComponent(jcbStartMinimized))
                .addGap(1, 1, 1)
                .addGroup(jpSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtServerAddr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jcbCheckVersionAtStartup)
                    .addComponent(jbCheckVersion))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jtAdminName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcbDisableAnonymousPrint))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jpSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(jtAdminPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4))
                    .addComponent(jtAdminPassword2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpSettingsLayout.createSequentialGroup()
                        .addGroup(jpSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jComboBoxLLProtocol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jpSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(jComboBoxLLDevice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jpSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(jComboBoxLLFPCBase, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jpSettingsLayout.createSequentialGroup()
                        .addComponent(jcbPoolEnable)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jpSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jtPoolDeadtime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jlPoolDeadtime)
                            .addComponent(jbClearPool))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jpSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jcbZFPLabServerAutoStart)
                            .addComponent(jbZFPStart)
                            .addComponent(jbZFPStop))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlCBIOLogLevel)
                    .addComponent(jComboBoxLLCBIO, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jtbCoERP, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jpSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jcbCoAutoStart)
                    .addComponent(jbCBIOStart)
                    .addComponent(jbCBIOStop))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jpSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbSave)
                    .addComponent(jbCancel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jtbMain.addTab("Настройки", jpSettings);

        jtaLicense.setEditable(false);
        jtaLicense.setColumns(20);
        jtaLicense.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        jtaLicense.setLineWrap(true);
        jtaLicense.setRows(5);
        jtaLicense.setText("Copyright 2015, EDA Ltd.\nThis program is distributed under the GNU General Public License v3.\n\n                    GNU GENERAL PUBLIC LICENSE\n                       Version 3, 29 June 2007\n\n Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>\n Everyone is permitted to copy and distribute verbatim copies\n of this license document, but changing it is not allowed.\n\n                            Preamble\n\n  The GNU General Public License is a free, copyleft license for\nsoftware and other kinds of works.\n\n  The licenses for most software and other practical works are designed\nto take away your freedom to share and change the works.  By contrast,\nthe GNU General Public License is intended to guarantee your freedom to\nshare and change all versions of a program--to make sure it remains free\nsoftware for all its users.  We, the Free Software Foundation, use the\nGNU General Public License for most of our software; it applies also to\nany other work released this way by its authors.  You can apply it to\nyour programs, too.\n\n  When we speak of free software, we are referring to freedom, not\nprice.  Our General Public Licenses are designed to make sure that you\nhave the freedom to distribute copies of free software (and charge for\nthem if you wish), that you receive source code or can get it if you\nwant it, that you can change the software or use pieces of it in new\nfree programs, and that you know you can do these things.\n\n  To protect your rights, we need to prevent others from denying you\nthese rights or asking you to surrender the rights.  Therefore, you have\ncertain responsibilities if you distribute copies of the software, or if\nyou modify it: responsibilities to respect the freedom of others.\n\n  For example, if you distribute copies of such a program, whether\ngratis or for a fee, you must pass on to the recipients the same\nfreedoms that you received.  You must make sure that they, too, receive\nor can get the source code.  And you must show them these terms so they\nknow their rights.\n\n  Developers that use the GNU GPL protect your rights with two steps:\n(1) assert copyright on the software, and (2) offer you this License\ngiving you legal permission to copy, distribute and/or modify it.\n\n  For the developers' and authors' protection, the GPL clearly explains\nthat there is no warranty for this free software.  For both users' and\nauthors' sake, the GPL requires that modified versions be marked as\nchanged, so that their problems will not be attributed erroneously to\nauthors of previous versions.\n\n  Some devices are designed to deny users access to install or run\nmodified versions of the software inside them, although the manufacturer\ncan do so.  This is fundamentally incompatible with the aim of\nprotecting users' freedom to change the software.  The systematic\npattern of such abuse occurs in the area of products for individuals to\nuse, which is precisely where it is most unacceptable.  Therefore, we\nhave designed this version of the GPL to prohibit the practice for those\nproducts.  If such problems arise substantially in other domains, we\nstand ready to extend this provision to those domains in future versions\nof the GPL, as needed to protect the freedom of users.\n\n  Finally, every program is threatened constantly by software patents.\nStates should not allow patents to restrict development and use of\nsoftware on general-purpose computers, but in those that do, we wish to\navoid the special danger that patents applied to a free program could\nmake it effectively proprietary.  To prevent this, the GPL assures that\npatents cannot be used to render the program non-free.\n\n  The precise terms and conditions for copying, distribution and\nmodification follow.\n\n                       TERMS AND CONDITIONS\n\n  0. Definitions.\n\n  \"This License\" refers to version 3 of the GNU General Public License.\n\n  \"Copyright\" also means copyright-like laws that apply to other kinds of\nworks, such as semiconductor masks.\n\n  \"The Program\" refers to any copyrightable work licensed under this\nLicense.  Each licensee is addressed as \"you\".  \"Licensees\" and\n\"recipients\" may be individuals or organizations.\n\n  To \"modify\" a work means to copy from or adapt all or part of the work\nin a fashion requiring copyright permission, other than the making of an\nexact copy.  The resulting work is called a \"modified version\" of the\nearlier work or a work \"based on\" the earlier work.\n\n  A \"covered work\" means either the unmodified Program or a work based\non the Program.\n\n  To \"propagate\" a work means to do anything with it that, without\npermission, would make you directly or secondarily liable for\ninfringement under applicable copyright law, except executing it on a\ncomputer or modifying a private copy.  Propagation includes copying,\ndistribution (with or without modification), making available to the\npublic, and in some countries other activities as well.\n\n  To \"convey\" a work means any kind of propagation that enables other\nparties to make or receive copies.  Mere interaction with a user through\na computer network, with no transfer of a copy, is not conveying.\n\n  An interactive user interface displays \"Appropriate Legal Notices\"\nto the extent that it includes a convenient and prominently visible\nfeature that (1) displays an appropriate copyright notice, and (2)\ntells the user that there is no warranty for the work (except to the\nextent that warranties are provided), that licensees may convey the\nwork under this License, and how to view a copy of this License.  If\nthe interface presents a list of user commands or options, such as a\nmenu, a prominent item in the list meets this criterion.\n\n  1. Source Code.\n\n  The \"source code\" for a work means the preferred form of the work\nfor making modifications to it.  \"Object code\" means any non-source\nform of a work.\n\n  A \"Standard Interface\" means an interface that either is an official\nstandard defined by a recognized standards body, or, in the case of\ninterfaces specified for a particular programming language, one that\nis widely used among developers working in that language.\n\n  The \"System Libraries\" of an executable work include anything, other\nthan the work as a whole, that (a) is included in the normal form of\npackaging a Major Component, but which is not part of that Major\nComponent, and (b) serves only to enable use of the work with that\nMajor Component, or to implement a Standard Interface for which an\nimplementation is available to the public in source code form.  A\n\"Major Component\", in this context, means a major essential component\n(kernel, window system, and so on) of the specific operating system\n(if any) on which the executable work runs, or a compiler used to\nproduce the work, or an object code interpreter used to run it.\n\n  The \"Corresponding Source\" for a work in object code form means all\nthe source code needed to generate, install, and (for an executable\nwork) run the object code and to modify the work, including scripts to\ncontrol those activities.  However, it does not include the work's\nSystem Libraries, or general-purpose tools or generally available free\nprograms which are used unmodified in performing those activities but\nwhich are not part of the work.  For example, Corresponding Source\nincludes interface definition files associated with source files for\nthe work, and the source code for shared libraries and dynamically\nlinked subprograms that the work is specifically designed to require,\nsuch as by intimate data communication or control flow between those\nsubprograms and other parts of the work.\n\n  The Corresponding Source need not include anything that users\ncan regenerate automatically from other parts of the Corresponding\nSource.\n\n  The Corresponding Source for a work in source code form is that\nsame work.\n\n  2. Basic Permissions.\n\n  All rights granted under this License are granted for the term of\ncopyright on the Program, and are irrevocable provided the stated\nconditions are met.  This License explicitly affirms your unlimited\npermission to run the unmodified Program.  The output from running a\ncovered work is covered by this License only if the output, given its\ncontent, constitutes a covered work.  This License acknowledges your\nrights of fair use or other equivalent, as provided by copyright law.\n\n  You may make, run and propagate covered works that you do not\nconvey, without conditions so long as your license otherwise remains\nin force.  You may convey covered works to others for the sole purpose\nof having them make modifications exclusively for you, or provide you\nwith facilities for running those works, provided that you comply with\nthe terms of this License in conveying all material for which you do\nnot control copyright.  Those thus making or running the covered works\nfor you must do so exclusively on your behalf, under your direction\nand control, on terms that prohibit them from making any copies of\nyour copyrighted material outside their relationship with you.\n\n  Conveying under any other circumstances is permitted solely under\nthe conditions stated below.  Sublicensing is not allowed; section 10\nmakes it unnecessary.\n\n  3. Protecting Users' Legal Rights From Anti-Circumvention Law.\n\n  No covered work shall be deemed part of an effective technological\nmeasure under any applicable law fulfilling obligations under article\n11 of the WIPO copyright treaty adopted on 20 December 1996, or\nsimilar laws prohibiting or restricting circumvention of such\nmeasures.\n\n  When you convey a covered work, you waive any legal power to forbid\ncircumvention of technological measures to the extent such circumvention\nis effected by exercising rights under this License with respect to\nthe covered work, and you disclaim any intention to limit operation or\nmodification of the work as a means of enforcing, against the work's\nusers, your or third parties' legal rights to forbid circumvention of\ntechnological measures.\n\n  4. Conveying Verbatim Copies.\n\n  You may convey verbatim copies of the Program's source code as you\nreceive it, in any medium, provided that you conspicuously and\nappropriately publish on each copy an appropriate copyright notice;\nkeep intact all notices stating that this License and any\nnon-permissive terms added in accord with section 7 apply to the code;\nkeep intact all notices of the absence of any warranty; and give all\nrecipients a copy of this License along with the Program.\n\n  You may charge any price or no price for each copy that you convey,\nand you may offer support or warranty protection for a fee.\n\n  5. Conveying Modified Source Versions.\n\n  You may convey a work based on the Program, or the modifications to\nproduce it from the Program, in the form of source code under the\nterms of section 4, provided that you also meet all of these conditions:\n\n    a) The work must carry prominent notices stating that you modified\n    it, and giving a relevant date.\n\n    b) The work must carry prominent notices stating that it is\n    released under this License and any conditions added under section\n    7.  This requirement modifies the requirement in section 4 to\n    \"keep intact all notices\".\n\n    c) You must license the entire work, as a whole, under this\n    License to anyone who comes into possession of a copy.  This\n    License will therefore apply, along with any applicable section 7\n    additional terms, to the whole of the work, and all its parts,\n    regardless of how they are packaged.  This License gives no\n    permission to license the work in any other way, but it does not\n    invalidate such permission if you have separately received it.\n\n    d) If the work has interactive user interfaces, each must display\n    Appropriate Legal Notices; however, if the Program has interactive\n    interfaces that do not display Appropriate Legal Notices, your\n    work need not make them do so.\n\n  A compilation of a covered work with other separate and independent\nworks, which are not by their nature extensions of the covered work,\nand which are not combined with it such as to form a larger program,\nin or on a volume of a storage or distribution medium, is called an\n\"aggregate\" if the compilation and its resulting copyright are not\nused to limit the access or legal rights of the compilation's users\nbeyond what the individual works permit.  Inclusion of a covered work\nin an aggregate does not cause this License to apply to the other\nparts of the aggregate.\n\n  6. Conveying Non-Source Forms.\n\n  You may convey a covered work in object code form under the terms\nof sections 4 and 5, provided that you also convey the\nmachine-readable Corresponding Source under the terms of this License,\nin one of these ways:\n\n    a) Convey the object code in, or embodied in, a physical product\n    (including a physical distribution medium), accompanied by the\n    Corresponding Source fixed on a durable physical medium\n    customarily used for software interchange.\n\n    b) Convey the object code in, or embodied in, a physical product\n    (including a physical distribution medium), accompanied by a\n    written offer, valid for at least three years and valid for as\n    long as you offer spare parts or customer support for that product\n    model, to give anyone who possesses the object code either (1) a\n    copy of the Corresponding Source for all the software in the\n    product that is covered by this License, on a durable physical\n    medium customarily used for software interchange, for a price no\n    more than your reasonable cost of physically performing this\n    conveying of source, or (2) access to copy the\n    Corresponding Source from a network server at no charge.\n\n    c) Convey individual copies of the object code with a copy of the\n    written offer to provide the Corresponding Source.  This\n    alternative is allowed only occasionally and noncommercially, and\n    only if you received the object code with such an offer, in accord\n    with subsection 6b.\n\n    d) Convey the object code by offering access from a designated\n    place (gratis or for a charge), and offer equivalent access to the\n    Corresponding Source in the same way through the same place at no\n    further charge.  You need not require recipients to copy the\n    Corresponding Source along with the object code.  If the place to\n    copy the object code is a network server, the Corresponding Source\n    may be on a different server (operated by you or a third party)\n    that supports equivalent copying facilities, provided you maintain\n    clear directions next to the object code saying where to find the\n    Corresponding Source.  Regardless of what server hosts the\n    Corresponding Source, you remain obligated to ensure that it is\n    available for as long as needed to satisfy these requirements.\n\n    e) Convey the object code using peer-to-peer transmission, provided\n    you inform other peers where the object code and Corresponding\n    Source of the work are being offered to the general public at no\n    charge under subsection 6d.\n\n  A separable portion of the object code, whose source code is excluded\nfrom the Corresponding Source as a System Library, need not be\nincluded in conveying the object code work.\n\n  A \"User Product\" is either (1) a \"consumer product\", which means any\ntangible personal property which is normally used for personal, family,\nor household purposes, or (2) anything designed or sold for incorporation\ninto a dwelling.  In determining whether a product is a consumer product,\ndoubtful cases shall be resolved in favor of coverage.  For a particular\nproduct received by a particular user, \"normally used\" refers to a\ntypical or common use of that class of product, regardless of the status\nof the particular user or of the way in which the particular user\nactually uses, or expects or is expected to use, the product.  A product\nis a consumer product regardless of whether the product has substantial\ncommercial, industrial or non-consumer uses, unless such uses represent\nthe only significant mode of use of the product.\n\n  \"Installation Information\" for a User Product means any methods,\nprocedures, authorization keys, or other information required to install\nand execute modified versions of a covered work in that User Product from\na modified version of its Corresponding Source.  The information must\nsuffice to ensure that the continued functioning of the modified object\ncode is in no case prevented or interfered with solely because\nmodification has been made.\n\n  If you convey an object code work under this section in, or with, or\nspecifically for use in, a User Product, and the conveying occurs as\npart of a transaction in which the right of possession and use of the\nUser Product is transferred to the recipient in perpetuity or for a\nfixed term (regardless of how the transaction is characterized), the\nCorresponding Source conveyed under this section must be accompanied\nby the Installation Information.  But this requirement does not apply\nif neither you nor any third party retains the ability to install\nmodified object code on the User Product (for example, the work has\nbeen installed in ROM).\n\n  The requirement to provide Installation Information does not include a\nrequirement to continue to provide support service, warranty, or updates\nfor a work that has been modified or installed by the recipient, or for\nthe User Product in which it has been modified or installed.  Access to a\nnetwork may be denied when the modification itself materially and\nadversely affects the operation of the network or violates the rules and\nprotocols for communication across the network.\n\n  Corresponding Source conveyed, and Installation Information provided,\nin accord with this section must be in a format that is publicly\ndocumented (and with an implementation available to the public in\nsource code form), and must require no special password or key for\nunpacking, reading or copying.\n\n  7. Additional Terms.\n\n  \"Additional permissions\" are terms that supplement the terms of this\nLicense by making exceptions from one or more of its conditions.\nAdditional permissions that are applicable to the entire Program shall\nbe treated as though they were included in this License, to the extent\nthat they are valid under applicable law.  If additional permissions\napply only to part of the Program, that part may be used separately\nunder those permissions, but the entire Program remains governed by\nthis License without regard to the additional permissions.\n\n  When you convey a copy of a covered work, you may at your option\nremove any additional permissions from that copy, or from any part of\nit.  (Additional permissions may be written to require their own\nremoval in certain cases when you modify the work.)  You may place\nadditional permissions on material, added by you to a covered work,\nfor which you have or can give appropriate copyright permission.\n\n  Notwithstanding any other provision of this License, for material you\nadd to a covered work, you may (if authorized by the copyright holders of\nthat material) supplement the terms of this License with terms:\n\n    a) Disclaiming warranty or limiting liability differently from the\n    terms of sections 15 and 16 of this License; or\n\n    b) Requiring preservation of specified reasonable legal notices or\n    author attributions in that material or in the Appropriate Legal\n    Notices displayed by works containing it; or\n\n    c) Prohibiting misrepresentation of the origin of that material, or\n    requiring that modified versions of such material be marked in\n    reasonable ways as different from the original version; or\n\n    d) Limiting the use for publicity purposes of names of licensors or\n    authors of the material; or\n\n    e) Declining to grant rights under trademark law for use of some\n    trade names, trademarks, or service marks; or\n\n    f) Requiring indemnification of licensors and authors of that\n    material by anyone who conveys the material (or modified versions of\n    it) with contractual assumptions of liability to the recipient, for\n    any liability that these contractual assumptions directly impose on\n    those licensors and authors.\n\n  All other non-permissive additional terms are considered \"further\nrestrictions\" within the meaning of section 10.  If the Program as you\nreceived it, or any part of it, contains a notice stating that it is\ngoverned by this License along with a term that is a further\nrestriction, you may remove that term.  If a license document contains\na further restriction but permits relicensing or conveying under this\nLicense, you may add to a covered work material governed by the terms\nof that license document, provided that the further restriction does\nnot survive such relicensing or conveying.\n\n  If you add terms to a covered work in accord with this section, you\nmust place, in the relevant source files, a statement of the\nadditional terms that apply to those files, or a notice indicating\nwhere to find the applicable terms.\n\n  Additional terms, permissive or non-permissive, may be stated in the\nform of a separately written license, or stated as exceptions;\nthe above requirements apply either way.\n\n  8. Termination.\n\n  You may not propagate or modify a covered work except as expressly\nprovided under this License.  Any attempt otherwise to propagate or\nmodify it is void, and will automatically terminate your rights under\nthis License (including any patent licenses granted under the third\nparagraph of section 11).\n\n  However, if you cease all violation of this License, then your\nlicense from a particular copyright holder is reinstated (a)\nprovisionally, unless and until the copyright holder explicitly and\nfinally terminates your license, and (b) permanently, if the copyright\nholder fails to notify you of the violation by some reasonable means\nprior to 60 days after the cessation.\n\n  Moreover, your license from a particular copyright holder is\nreinstated permanently if the copyright holder notifies you of the\nviolation by some reasonable means, this is the first time you have\nreceived notice of violation of this License (for any work) from that\ncopyright holder, and you cure the violation prior to 30 days after\nyour receipt of the notice.\n\n  Termination of your rights under this section does not terminate the\nlicenses of parties who have received copies or rights from you under\nthis License.  If your rights have been terminated and not permanently\nreinstated, you do not qualify to receive new licenses for the same\nmaterial under section 10.\n\n  9. Acceptance Not Required for Having Copies.\n\n  You are not required to accept this License in order to receive or\nrun a copy of the Program.  Ancillary propagation of a covered work\noccurring solely as a consequence of using peer-to-peer transmission\nto receive a copy likewise does not require acceptance.  However,\nnothing other than this License grants you permission to propagate or\nmodify any covered work.  These actions infringe copyright if you do\nnot accept this License.  Therefore, by modifying or propagating a\ncovered work, you indicate your acceptance of this License to do so.\n\n  10. Automatic Licensing of Downstream Recipients.\n\n  Each time you convey a covered work, the recipient automatically\nreceives a license from the original licensors, to run, modify and\npropagate that work, subject to this License.  You are not responsible\nfor enforcing compliance by third parties with this License.\n\n  An \"entity transaction\" is a transaction transferring control of an\norganization, or substantially all assets of one, or subdividing an\norganization, or merging organizations.  If propagation of a covered\nwork results from an entity transaction, each party to that\ntransaction who receives a copy of the work also receives whatever\nlicenses to the work the party's predecessor in interest had or could\ngive under the previous paragraph, plus a right to possession of the\nCorresponding Source of the work from the predecessor in interest, if\nthe predecessor has it or can get it with reasonable efforts.\n\n  You may not impose any further restrictions on the exercise of the\nrights granted or affirmed under this License.  For example, you may\nnot impose a license fee, royalty, or other charge for exercise of\nrights granted under this License, and you may not initiate litigation\n(including a cross-claim or counterclaim in a lawsuit) alleging that\nany patent claim is infringed by making, using, selling, offering for\nsale, or importing the Program or any portion of it.\n\n  11. Patents.\n\n  A \"contributor\" is a copyright holder who authorizes use under this\nLicense of the Program or a work on which the Program is based.  The\nwork thus licensed is called the contributor's \"contributor version\".\n\n  A contributor's \"essential patent claims\" are all patent claims\nowned or controlled by the contributor, whether already acquired or\nhereafter acquired, that would be infringed by some manner, permitted\nby this License, of making, using, or selling its contributor version,\nbut do not include claims that would be infringed only as a\nconsequence of further modification of the contributor version.  For\npurposes of this definition, \"control\" includes the right to grant\npatent sublicenses in a manner consistent with the requirements of\nthis License.\n\n  Each contributor grants you a non-exclusive, worldwide, royalty-free\npatent license under the contributor's essential patent claims, to\nmake, use, sell, offer for sale, import and otherwise run, modify and\npropagate the contents of its contributor version.\n\n  In the following three paragraphs, a \"patent license\" is any express\nagreement or commitment, however denominated, not to enforce a patent\n(such as an express permission to practice a patent or covenant not to\nsue for patent infringement).  To \"grant\" such a patent license to a\nparty means to make such an agreement or commitment not to enforce a\npatent against the party.\n\n  If you convey a covered work, knowingly relying on a patent license,\nand the Corresponding Source of the work is not available for anyone\nto copy, free of charge and under the terms of this License, through a\npublicly available network server or other readily accessible means,\nthen you must either (1) cause the Corresponding Source to be so\navailable, or (2) arrange to deprive yourself of the benefit of the\npatent license for this particular work, or (3) arrange, in a manner\nconsistent with the requirements of this License, to extend the patent\nlicense to downstream recipients.  \"Knowingly relying\" means you have\nactual knowledge that, but for the patent license, your conveying the\ncovered work in a country, or your recipient's use of the covered work\nin a country, would infringe one or more identifiable patents in that\ncountry that you have reason to believe are valid.\n\n  If, pursuant to or in connection with a single transaction or\narrangement, you convey, or propagate by procuring conveyance of, a\ncovered work, and grant a patent license to some of the parties\nreceiving the covered work authorizing them to use, propagate, modify\nor convey a specific copy of the covered work, then the patent license\nyou grant is automatically extended to all recipients of the covered\nwork and works based on it.\n\n  A patent license is \"discriminatory\" if it does not include within\nthe scope of its coverage, prohibits the exercise of, or is\nconditioned on the non-exercise of one or more of the rights that are\nspecifically granted under this License.  You may not convey a covered\nwork if you are a party to an arrangement with a third party that is\nin the business of distributing software, under which you make payment\nto the third party based on the extent of your activity of conveying\nthe work, and under which the third party grants, to any of the\nparties who would receive the covered work from you, a discriminatory\npatent license (a) in connection with copies of the covered work\nconveyed by you (or copies made from those copies), or (b) primarily\nfor and in connection with specific products or compilations that\ncontain the covered work, unless you entered into that arrangement,\nor that patent license was granted, prior to 28 March 2007.\n\n  Nothing in this License shall be construed as excluding or limiting\nany implied license or other defenses to infringement that may\notherwise be available to you under applicable patent law.\n\n  12. No Surrender of Others' Freedom.\n\n  If conditions are imposed on you (whether by court order, agreement or\notherwise) that contradict the conditions of this License, they do not\nexcuse you from the conditions of this License.  If you cannot convey a\ncovered work so as to satisfy simultaneously your obligations under this\nLicense and any other pertinent obligations, then as a consequence you may\nnot convey it at all.  For example, if you agree to terms that obligate you\nto collect a royalty for further conveying from those to whom you convey\nthe Program, the only way you could satisfy both those terms and this\nLicense would be to refrain entirely from conveying the Program.\n\n  13. Use with the GNU Affero General Public License.\n\n  Notwithstanding any other provision of this License, you have\npermission to link or combine any covered work with a work licensed\nunder version 3 of the GNU Affero General Public License into a single\ncombined work, and to convey the resulting work.  The terms of this\nLicense will continue to apply to the part which is the covered work,\nbut the special requirements of the GNU Affero General Public License,\nsection 13, concerning interaction through a network will apply to the\ncombination as such.\n\n  14. Revised Versions of this License.\n\n  The Free Software Foundation may publish revised and/or new versions of\nthe GNU General Public License from time to time.  Such new versions will\nbe similar in spirit to the present version, but may differ in detail to\naddress new problems or concerns.\n\n  Each version is given a distinguishing version number.  If the\nProgram specifies that a certain numbered version of the GNU General\nPublic License \"or any later version\" applies to it, you have the\noption of following the terms and conditions either of that numbered\nversion or of any later version published by the Free Software\nFoundation.  If the Program does not specify a version number of the\nGNU General Public License, you may choose any version ever published\nby the Free Software Foundation.\n\n  If the Program specifies that a proxy can decide which future\nversions of the GNU General Public License can be used, that proxy's\npublic statement of acceptance of a version permanently authorizes you\nto choose that version for the Program.\n\n  Later license versions may give you additional or different\npermissions.  However, no additional obligations are imposed on any\nauthor or copyright holder as a result of your choosing to follow a\nlater version.\n\n  15. Disclaimer of Warranty.\n\n  THERE IS NO WARRANTY FOR THE PROGRAM, TO THE EXTENT PERMITTED BY\nAPPLICABLE LAW.  EXCEPT WHEN OTHERWISE STATED IN WRITING THE COPYRIGHT\nHOLDERS AND/OR OTHER PARTIES PROVIDE THE PROGRAM \"AS IS\" WITHOUT WARRANTY\nOF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING, BUT NOT LIMITED TO,\nTHE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR\nPURPOSE.  THE ENTIRE RISK AS TO THE QUALITY AND PERFORMANCE OF THE PROGRAM\nIS WITH YOU.  SHOULD THE PROGRAM PROVE DEFECTIVE, YOU ASSUME THE COST OF\nALL NECESSARY SERVICING, REPAIR OR CORRECTION.\n\n  16. Limitation of Liability.\n\n  IN NO EVENT UNLESS REQUIRED BY APPLICABLE LAW OR AGREED TO IN WRITING\nWILL ANY COPYRIGHT HOLDER, OR ANY OTHER PARTY WHO MODIFIES AND/OR CONVEYS\nTHE PROGRAM AS PERMITTED ABOVE, BE LIABLE TO YOU FOR DAMAGES, INCLUDING ANY\nGENERAL, SPECIAL, INCIDENTAL OR CONSEQUENTIAL DAMAGES ARISING OUT OF THE\nUSE OR INABILITY TO USE THE PROGRAM (INCLUDING BUT NOT LIMITED TO LOSS OF\nDATA OR DATA BEING RENDERED INACCURATE OR LOSSES SUSTAINED BY YOU OR THIRD\nPARTIES OR A FAILURE OF THE PROGRAM TO OPERATE WITH ANY OTHER PROGRAMS),\nEVEN IF SUCH HOLDER OR OTHER PARTY HAS BEEN ADVISED OF THE POSSIBILITY OF\nSUCH DAMAGES.\n\n  17. Interpretation of Sections 15 and 16.\n\n  If the disclaimer of warranty and limitation of liability provided\nabove cannot be given local legal effect according to their terms,\nreviewing courts shall apply local law that most closely approximates\nan absolute waiver of all civil liability in connection with the\nProgram, unless a warranty or assumption of liability accompanies a\ncopy of the Program in return for a fee.\n\n                     END OF TERMS AND CONDITIONS\n\n            How to Apply These Terms to Your New Programs\n\n  If you develop a new program, and you want it to be of the greatest\npossible use to the public, the best way to achieve this is to make it\nfree software which everyone can redistribute and change under these terms.\n\n  To do so, attach the following notices to the program.  It is safest\nto attach them to the start of each source file to most effectively\nstate the exclusion of warranty; and each file should have at least\nthe \"copyright\" line and a pointer to where the full notice is found.\n\n    <one line to give the program's name and a brief idea of what it does.>\n    Copyright (C) <year>  <name of author>\n\n    This program is free software: you can redistribute it and/or modify\n    it under the terms of the GNU General Public License as published by\n    the Free Software Foundation, either version 3 of the License, or\n    (at your option) any later version.\n\n    This program is distributed in the hope that it will be useful,\n    but WITHOUT ANY WARRANTY; without even the implied warranty of\n    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n    GNU General Public License for more details.\n\n    You should have received a copy of the GNU General Public License\n    along with this program.  If not, see <http://www.gnu.org/licenses/>.\n\nAlso add information on how to contact you by electronic and paper mail.\n\n  If the program does terminal interaction, make it output a short\nnotice like this when it starts in an interactive mode:\n\n    <program>  Copyright (C) <year>  <name of author>\n    This program comes with ABSOLUTELY NO WARRANTY; for details type `show w'.\n    This is free software, and you are welcome to redistribute it\n    under certain conditions; type `show c' for details.\n\nThe hypothetical commands `show w' and `show c' should show the appropriate\nparts of the General Public License.  Of course, your program's commands\nmight be different; for a GUI interface, you would use an \"about box\".\n\n  You should also get your employer (if you work as a programmer) or school,\nif any, to sign a \"copyright disclaimer\" for the program, if necessary.\nFor more information on this, and how to apply and follow the GNU GPL, see\n<http://www.gnu.org/licenses/>.\n\n  The GNU General Public License does not permit incorporating your program\ninto proprietary programs.  If your program is a subroutine library, you\nmay consider it more useful to permit linking proprietary applications with\nthe library.  If this is what you want to do, use the GNU Lesser General\nPublic License instead of this License.  But first, please read\n<http://www.gnu.org/philosophy/why-not-lgpl.html>.\n");
        jScrollPane3.setViewportView(jtaLicense);

        jtbMain.addTab("Лиценз", jScrollPane3);

        jlAStatus.setFont(new java.awt.Font("Consolas", 0, 10)); // NOI18N
        jlAStatus.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jlAStatus.setText("Статус");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jbStart)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jbStop)
                .addGap(8, 8, 8))
            .addComponent(jtbMain)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(78, 78, 78)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jlAppVersion)
                            .addComponent(jlBuildInfo)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(jlAppName)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jlAStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 371, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jlAppName)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jlAppVersion)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jlBuildInfo))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jlAStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(6, 6, 6)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jtbMain)
                .addGap(4, 4, 4)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbStart)
                    .addComponent(jbStop))
                .addContainerGap())
        );

        jtbMain.getAccessibleContext().setAccessibleName("Лог на достъпа");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void updateAStatusInfo() {
        String aStatusTooltip = "Сървър: "+(FPServer.mainComponent.isStarted()?"Стартиран":"Спрян");
        String aStatus = "Сървър: "+(FPServer.mainComponent.isStarted()?"<span style='color: green; font-weight: bold;'>Стартиран</span>":"<span style='color: red; font-weight: bold;'>Спрян</span>");
        aStatus = aStatus +"<br/>Колибри ERP:";
        aStatusTooltip = aStatusTooltip+"\nКолибри ERP:";
        if (FPServer.application.isCBIOServiceStarted()) {
            for (int i = 0; i < FPServer.getCBIO_SERVICE_COUNT(); i++) {
                CBIOService.ConnectionState state = FPServer.application.getCBIOServiceStatus(i);
                if (!state.equals(ConnectionState.NA)) {
                    String stateColor = state.equals(ConnectionState.JOINED)?"green":"red";
                    aStatus = aStatus + " <span style='color: "+stateColor+"; font-weight: bold;'>" + Integer.toString(i+1)+"-"+state.toString()+"</span>";
                    aStatusTooltip = aStatusTooltip + " " + Integer.toString(i+1)+"-"+state.toString();
                }
            }
        } else {
            aStatus = aStatus + " <span style='color: red; font-weight: bold;'>Спрян</span>";
            aStatusTooltip = aStatusTooltip + " Спрян";
        }
        if (ZFPLabServer.isStarted()) {
            aStatus = aStatus +"<br/>Тремол: "+"Стартиран";
            aStatusTooltip = aStatusTooltip + "\nТремол: "+"Стартиран";;
        }    
        jlAStatus.setText("<html>"+aStatus+"</html>");
        jlAStatus.setToolTipText(aStatusTooltip);
    }
    
    private void adjustStartStopControls() {
        jbStart.setEnabled(FPServer.mainComponent.isStopped());
        jbStop.setEnabled(FPServer.mainComponent.isStarted());
        jbZFPStart.setEnabled(!ZFPLabServer.isStarted());
        jbZFPStop.setEnabled(ZFPLabServer.isStarted());
        jbCBIOStart.setEnabled(!FPServer.application.isCBIOServiceStarted());
        jbCBIOStop.setEnabled(FPServer.application.isCBIOServiceStarted());
        updateAStatusInfo();
        adjustSysTray();
    }
    
    private void startZFPServer() {
        try {
            ZFPLabServer.start();
        } catch (Exception ex) {
            Logger.getLogger(ControlPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        adjustStartStopControls();
    }

    private void stopZFPServer() {
        ZFPLabServer.stop();
        adjustStartStopControls();
    }
    
    private void startServer() {
        FPServer.application.startServer();
        adjustStartStopControls();
    }
    
    private void stopServer() {
        FPServer.application.stopServer();
        adjustStartStopControls();
    }
    
    private void jbStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbStartActionPerformed
        startServer();
    }//GEN-LAST:event_jbStartActionPerformed

    private void jbStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbStopActionPerformed
        stopServer();
    }//GEN-LAST:event_jbStopActionPerformed

    private void jtServerPortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtServerPortActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtServerPortActionPerformed

    private void jtAdminNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtAdminNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtAdminNameActionPerformed

    private void jtbMainFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtbMainFocusGained
        //adjustSettingsControls();
        adjustStartStopControls();
    }//GEN-LAST:event_jtbMainFocusGained

    private void jbSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbSaveActionPerformed
        Properties prop = FPServer.application.getAppProperties();
        prop.setProperty("ServerPort", jtServerPort.getText());
        prop.setProperty("ServerPortSSL", jtServerPortSSL.getText());
        prop.setProperty("ServerAddr", jtServerAddr.getText());
        prop.setProperty("AdminUser", jtAdminName.getText());
        String pass1 = String.copyValueOf(jtAdminPassword.getPassword());
        String pass2 = String.copyValueOf(jtAdminPassword2.getPassword());
        if (pass1.length() > 0) {
            if (pass1.equals(pass2)) {
                prop.setProperty("AdminPass", DigestUtils.toMd5(pass1));
            } else
                JOptionPane.showMessageDialog(null, "Паролите не съвпадат!");
        }
        prop.setProperty("DisableAnonymous", jcbDisableAnonymousPrint.isSelected()?"1":"0");
        prop.setProperty("UseSSL", jcbUseSSL.isSelected()?"1":"0");
        prop.setProperty("UseHTTP", jcbUseHTTP.isSelected()?"1":"0");
        prop.setProperty("StartMinimized", jcbStartMinimized.isSelected()?"1":"0");
        prop.setProperty("CheckVersionAtStartup", jcbCheckVersionAtStartup.isSelected()?"1":"0");
        prop.setProperty("LLDevice", (String)jComboBoxLLDevice.getSelectedItem());
        prop.setProperty("LLProtocol", (String)jComboBoxLLProtocol.getSelectedItem());
        prop.setProperty("LLFPCBase", (String)jComboBoxLLFPCBase.getSelectedItem());
        prop.setProperty("LLCBIOService", (String)jComboBoxLLCBIO.getSelectedItem());
        prop.setProperty("PoolEnabled", jcbPoolEnable.isSelected()?"1":"0");
        prop.setProperty("ZFPLabServerAutoStart", jcbZFPLabServerAutoStart.isSelected()?"1":"0");
        prop.setProperty("PoolDeadtime", jtPoolDeadtime.getText());
        
        prop.setProperty("CoAutoStart", jcbCoAutoStart.isSelected()?"1":"0");
        // Colibri ERP 1
        prop.setProperty("CoURL", jtCoURL.getText());
        prop.setProperty("CoUser", jtCoUser.getText());
        String coPass = String.copyValueOf(jtCoPass.getPassword());
        if (coPass.length() > 0) {
            // TODO: Encrypt
            prop.setProperty("CoPass", Base64.getEncoder().encodeToString(coPass.getBytes()));
        }
        // Colibri ERP 2
        prop.setProperty("CoURL_1", jtCoURL1.getText());
        prop.setProperty("CoUser_1", jtCoUser1.getText());
        coPass = String.copyValueOf(jtCoPass1.getPassword());
        if (coPass.length() > 0) {
            // TODO: Encrypt
            prop.setProperty("CoPass_1", Base64.getEncoder().encodeToString(coPass.getBytes()));
        }
        // Colibri ERP 3
        prop.setProperty("CoURL_2", jtCoURL2.getText());
        prop.setProperty("CoUser_2", jtCoUser2.getText());
        coPass = String.copyValueOf(jtCoPass2.getPassword());
        if (coPass.length() > 0) {
            // TODO: Encrypt
            prop.setProperty("CoPass_2", Base64.getEncoder().encodeToString(coPass.getBytes()));
        }
        // Colibri ERP 4
        prop.setProperty("CoURL_3", jtCoURL3.getText());
        prop.setProperty("CoUser_3", jtCoUser3.getText());
        coPass = String.copyValueOf(jtCoPass3.getPassword());
        if (coPass.length() > 0) {
            // TODO: Encrypt
            prop.setProperty("CoPass_3", Base64.getEncoder().encodeToString(coPass.getBytes()));
        }

        
        FPServer.application.updateProperties();
    }//GEN-LAST:event_jbSaveActionPerformed

    private void jtServerAddrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtServerAddrActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtServerAddrActionPerformed

    private void jlGoAdminMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jlGoAdminMouseClicked
        // TODO add your handling code here:
        try {
            Desktop.getDesktop().browse(FPServer.application.getAdminURL());
        } catch (URISyntaxException | IOException ex) {
            //It looks like there's a problem
        }        
    }//GEN-LAST:event_jlGoAdminMouseClicked

    private void exitApplication(boolean requreConfirmation) {
        int confirmed = JOptionPane.YES_OPTION;
        if (requreConfirmation) {
            confirmed = JOptionPane.showConfirmDialog(
                null
                , "Наистина ли искате да излезете от EDA FPGate Server?"
                , "Потвърждение"
                , JOptionPane.YES_NO_OPTION
            );
        }     
        if (confirmed == JOptionPane.YES_OPTION) {
            ZFPLabServer.stop();
            FPServer.application.stopCBIOService();
            System.exit(0);
        }    
    }
    private void exitApplication() {
        exitApplication(true);
    }
    
    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        exitApplication();
    }//GEN-LAST:event_formWindowClosing

    private void jcbStartMinimizedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbStartMinimizedActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jcbStartMinimizedActionPerformed

    private void jbCheckVersionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbCheckVersionActionPerformed
        FPServer.application.checkAppVersion(true);
    }//GEN-LAST:event_jbCheckVersionActionPerformed

    private void jtPoolDeadtimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtPoolDeadtimeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtPoolDeadtimeActionPerformed

    private void jbClearPoolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbClearPoolActionPerformed
        // TODO add your handling code here:
        FPPrinterPool.clear();
    }//GEN-LAST:event_jbClearPoolActionPerformed

    private void jcbZFPLabServerAutoStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbZFPLabServerAutoStartActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jcbZFPLabServerAutoStartActionPerformed

    private void jbZFPStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbZFPStartActionPerformed
        // TODO add your handling code here:
        startZFPServer();
    }//GEN-LAST:event_jbZFPStartActionPerformed

    private void jbZFPStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbZFPStopActionPerformed
        // TODO add your handling code here:
        stopZFPServer();
    }//GEN-LAST:event_jbZFPStopActionPerformed

    private void jlAppNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jlAppNameMouseClicked
        // TODO add your handling code here:
        testRIAP();
    }//GEN-LAST:event_jlAppNameMouseClicked

    private void jbCBIOStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbCBIOStartActionPerformed
        // TODO add your handling code here:
        FPServer.application.startCBIOService();
        adjustStartStopControls();
    }//GEN-LAST:event_jbCBIOStartActionPerformed

    private void jbCBIOStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbCBIOStopActionPerformed
        // TODO add your handling code here:
        FPServer.application.stopCBIOService();
        adjustStartStopControls();
    }//GEN-LAST:event_jbCBIOStopActionPerformed

    private void jbCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbCancelActionPerformed
        // TODO add your handling code here:
        adjustSettingsControls();
        adjustStartStopControls();
    }//GEN-LAST:event_jbCancelActionPerformed

    private void jcbUseSSLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbUseSSLActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jcbUseSSLActionPerformed

    private void testRIAP() {
//        Context context;
//        FPServer.mainComponent.getContext().getClientDispatcher().get("riap://print/");

/*
    ClientResource cr = new ClientResource("riap://component/");
    Representation repr = cr.get();
        try {
            System.out.println(repr.getText());
        } catch (IOException ex) {
            Logger.getLogger(ControlPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
*/
        
        ClientResource res = new ClientResource(LocalReference.createRiapReference(LocalReference.RIAP_COMPONENT, "/print/"));
        PrintRequest prequest = new PrintRequest();
        ObjectMapper mapper = new ObjectMapper();
        try {
            String jsonRequest = "{\"method\":\"PrinterStatus\",\"params\":{\"arguments\":[\"\"],\"printer\":{\"ID\":\"DP150KL\",\"Model\":\"\",\"Params\":{}}},\"id\":\"\"}";
            prequest = mapper.readValue(jsonRequest, PrintRequest.class);
            Representation repr = res.post(prequest, MediaType.APPLICATION_JSON);
//            PrintResponse response = res.type("application/json").post(PrintResponse.class, jsonRequest);
            
//            Representation repr = res.get(MediaType.ALL); 
            if (repr != null)
                System.out.println(repr.getText());
        } catch (IOException ex) {
            Logger.getLogger(ControlPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        res.release();
    }
    synchronized public void OnCBIOStateChange(int svcNum, ConnectionState state) {
        // Colibri ERP 1
        jtCBIOServiceState.setText(null);
        jtCBIOServiceState.setText(FPServer.application.getCBIOServiceStatus(0).toString());
//        jtbCoERP.setIgnoreRepaint(true);
        CBIOService.ConnectionState aState = FPServer.application.getCBIOServiceStatus(0);
        String stateColor = aState.equals(CBIOService.ConnectionState.JOINED)?"green":(aState.equals(CBIOService.ConnectionState.NA)?"":"red");
//        jtbCoERP.setTitleAt(0, "<html>Колибри ERP 1 <span style='color: "+stateColor+";'>("+FPServer.application.getCBIOServiceStatus(0).abbr()+")<span></html>");
        jtbCoERP.setTitleAt(0, "Колибри ERP 1 ("+FPServer.application.getCBIOServiceStatus(0).abbr()+")");
        // Colibri ERP 2
        jtCBIOServiceState1.setText(null);
        jtCBIOServiceState1.setText(FPServer.application.getCBIOServiceStatus(1).toString());
//        jtbCoERP.setTitleAt(1, "<html>Колибри ERP 2 ("+FPServer.application.getCBIOServiceStatus(1).abbr()+")</html>");
        jtbCoERP.setTitleAt(1, "Колибри ERP 2 ("+FPServer.application.getCBIOServiceStatus(1).abbr()+")");
        // Colibri ERP 3
        jtCBIOServiceState2.setText(null);
        jtCBIOServiceState2.setText(FPServer.application.getCBIOServiceStatus(2).toString());
        jtbCoERP.setTitleAt(2, "Колибри ERP 3 ("+FPServer.application.getCBIOServiceStatus(2).abbr()+")");
        // Colibri ERP 4
        jtCBIOServiceState3.setText(null);
        jtCBIOServiceState3.setText(FPServer.application.getCBIOServiceStatus(3).toString());
        jtbCoERP.setTitleAt(3, "Колибри ERP 4 ("+FPServer.application.getCBIOServiceStatus(3).abbr()+")");
//        jtbCoERP.setIgnoreRepaint(false);

        updateAStatusInfo();
    }
    
    private void adjustSettingsControls() {
        Properties prop = FPServer.application.getAppProperties();
        jtServerPort.setText(prop.getProperty("ServerPort"));
        jtServerPortSSL.setText(prop.getProperty("ServerPortSSL"));
        jtServerAddr.setText(prop.getProperty("ServerAddr"));
        jtAdminName.setText(prop.getProperty("AdminUser"));
        jtAdminPassword.setText("");
        jtAdminPassword2.setText("");
        jcbDisableAnonymousPrint.setSelected(prop.getProperty("DisableAnonymous").equals("1"));
        jcbUseSSL.setSelected(prop.getProperty("UseSSL").equals("1"));
        jcbUseHTTP.setSelected(prop.getProperty("UseHTTP").equals("1"));
        jcbStartMinimized.setSelected(prop.getProperty("StartMinimized").equals("1"));
        jcbCheckVersionAtStartup.setSelected(prop.getProperty("CheckVersionAtStartup").equals("1"));
        jcbPoolEnable.setSelected(prop.getProperty("PoolEnabled").equals("1"));
        jtPoolDeadtime.setText(prop.getProperty("PoolDeadtime"));

        jcbZFPLabServerAutoStart.setSelected(prop.getProperty("ZFPLabServerAutoStart").equals("1"));
        // Colibri ERP 1
        jcbCoAutoStart.setSelected(prop.getProperty("CoAutoStart").equals("1"));
        jtCoURL.setText(prop.getProperty("CoURL"));
        jtCoUser.setText(prop.getProperty("CoUser"));
        jtCBIOServiceState.setText(FPServer.application.getCBIOServiceStatus(0).toString());
        // Colibri ERP 2
        jtCoURL1.setText(prop.getProperty("CoURL_1"));
        jtCoUser1.setText(prop.getProperty("CoUser_1"));
        jtCBIOServiceState1.setText(FPServer.application.getCBIOServiceStatus(1).toString());
        // Colibri ERP 3
        jtCoURL2.setText(prop.getProperty("CoURL_2"));
        jtCoUser2.setText(prop.getProperty("CoUser_2"));
        jtCBIOServiceState2.setText(FPServer.application.getCBIOServiceStatus(2).toString());
        // Colibri ERP 4
        jtCoURL3.setText(prop.getProperty("CoURL_3"));
        jtCoUser3.setText(prop.getProperty("CoUser_3"));
        jtCBIOServiceState3.setText(FPServer.application.getCBIOServiceStatus(3).toString());
        
        FPServer.application.addOnStateChangeListener(this::OnCBIOStateChange);
        String[] levels = new String[] {
            Level.OFF.getName()
            , Level.SEVERE.getName()
            , Level.WARNING.getName()
            , Level.INFO.getName()
            , Level.CONFIG.getName()
            , Level.FINE.getName()
            , Level.FINEST.getName()
            , Level.ALL.getName()
        };
        jComboBoxLLDevice.removeAllItems();
        jComboBoxLLProtocol.removeAllItems();
        jComboBoxLLFPCBase.removeAllItems();
        jComboBoxLLCBIO.removeAllItems();
        for (int i =0; i < levels.length; i++) {
            jComboBoxLLDevice.addItem(levels[i]);
            jComboBoxLLProtocol.addItem(levels[i]);
            jComboBoxLLFPCBase.addItem(levels[i]);
            jComboBoxLLCBIO.addItem(levels[i]);
        }
        
        jComboBoxLLDevice.setSelectedItem(prop.getProperty("LLDevice"));
        jComboBoxLLProtocol.setSelectedItem(prop.getProperty("LLProtocol"));
        jComboBoxLLFPCBase.setSelectedItem(prop.getProperty("LLFPCBase"));
        jComboBoxLLCBIO.setSelectedItem(prop.getProperty("LLCBIOService"));
    }
    
    
    public void notifyChange() {
        adjustSettingsControls();
        adjustStartStopControls();
    }
    
    TrayIcon trayIcon;
    SystemTray tray;
    Image imagePrinterOn = null;
    Image imagePrinterOff = null;
    MenuItem menuItemStart = null;
    MenuItem menuItemStop = null;
    
    public void hideToSystemTray(){
/*
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch(Exception e){
            System.out.println("Unable to set LookAndFeel");
        }
*/        
        
        try {
            imagePrinterOn = ImageIO.read(getClass().getResource("/images/printer-on-512.png"));
            imagePrinterOff = ImageIO.read(getClass().getResource("/images/printer-off-512.png"));
        } catch (IOException ex) {
            Logger.getLogger(ControlPanel.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        if(SystemTray.isSupported()){
            System.out.println("System tray supported");
            tray=SystemTray.getSystemTray();

//            Image image=Toolkit.getDefaultToolkit().getImage("/media/faisal/DukeImg/Duke256.png");
            PopupMenu popup=new PopupMenu();
            final ActionListener openListener = 
                    new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            setVisible(true);
                            setExtendedState(JFrame.NORMAL);
                        }
                    };
                    
            MenuItem defaultItem = new MenuItem("EDA FPGate Server Контролен панел") {{
                addActionListener(openListener);
            }};
            defaultItem.setFont(getFont().deriveFont(Font.BOLD));
            popup.add(defaultItem);
/*
            popup.add(
                new MenuItem("EDA FPGate Server Control Panel") {{
                    setFont(getFont().deriveFont(Font.BOLD));
                    addActionListener(openListener);
                }}
            );
*/        
            popup.addSeparator();
/*            
            popup.add(
                new MenuItem("Open") {{
                    addActionListener(openListener);
                }}
            );
            popup.addSeparator();
*/        
            menuItemStart = new MenuItem("Старт") {{
                    addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            startServer();
                        }
                    });
            }};
            popup.add(menuItemStart);
            menuItemStop = new MenuItem("Стоп") {{
                    addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            stopServer();
                        }
                    });
            }};
            popup.add(menuItemStop);
            popup.addSeparator();
            popup.add(
                new MenuItem("Изход") {{
                    addActionListener(
                        new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                exitApplication();
                            }
                        }
                    );
                }}
            );
            trayIcon=new TrayIcon(FPServer.application.isStarted()?imagePrinterOn:imagePrinterOff, "EDA FPGate Server", popup);
            trayIcon.addActionListener(openListener);
            trayIcon.setImageAutoSize(true);
        }else{
            System.out.println("system tray not supported");
        }
        addWindowStateListener(new WindowStateListener() {
            public void windowStateChanged(WindowEvent e) {
                if(e.getNewState()==ICONIFIED){
                    try {
                        tray.add(trayIcon);
                        setVisible(false);
                        System.out.println("added to SystemTray");
                    } catch (AWTException ex) {
                        System.out.println("unable to add to tray");
                    }
                }
        if(e.getNewState()==7){
                    try{
            tray.add(trayIcon);
            setVisible(false);
            System.out.println("added to SystemTray");
            }catch(AWTException ex){
            System.out.println("unable to add to system tray");
        }
            }
        if(e.getNewState()==MAXIMIZED_BOTH){
                    tray.remove(trayIcon);
                    setVisible(true);
                    System.out.println("Tray icon removed");
                }
                if(e.getNewState()==NORMAL){
                    tray.remove(trayIcon);
                    setVisible(true);
                    System.out.println("Tray icon removed");
                }
            }
        });
//        setIconImage(Toolkit.getDefaultToolkit().getImage("Duke256.png"));
//        setIconImage(FPServer.application.isStarted()?imagePrinterOn:imagePrinterOff);
        adjustSysTray();
        setVisible(true);
//        setSize(300, 200);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public void adjustSysTray() {
        trayIcon.setImage(FPServer.application.isStarted()?imagePrinterOn:imagePrinterOff);
        setIconImage(FPServer.application.isStarted()?imagePrinterOn:imagePrinterOff);
        trayIcon.setToolTip("EDA FPGate Server "+jlAStatus.getToolTipText());
        menuItemStart.setEnabled(!FPServer.application.isStarted());
        menuItemStop.setEnabled(FPServer.application.isStarted());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> jComboBoxLLCBIO;
    private javax.swing.JComboBox<String> jComboBoxLLDevice;
    private javax.swing.JComboBox<String> jComboBoxLLFPCBase;
    private javax.swing.JComboBox<String> jComboBoxLLProtocol;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JButton jbCBIOStart;
    private javax.swing.JButton jbCBIOStop;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbCheckVersion;
    private javax.swing.JButton jbClearPool;
    private javax.swing.JButton jbSave;
    private javax.swing.JButton jbStart;
    private javax.swing.JButton jbStop;
    private javax.swing.JButton jbZFPStart;
    private javax.swing.JButton jbZFPStop;
    private javax.swing.JCheckBox jcbCheckVersionAtStartup;
    private javax.swing.JCheckBox jcbCoAutoStart;
    private javax.swing.JCheckBox jcbDisableAnonymousPrint;
    private javax.swing.JCheckBox jcbPoolEnable;
    private javax.swing.JCheckBox jcbStartMinimized;
    private javax.swing.JCheckBox jcbUseHTTP;
    private javax.swing.JCheckBox jcbUseSSL;
    private javax.swing.JCheckBox jcbZFPLabServerAutoStart;
    private javax.swing.JLabel jlAStatus;
    private javax.swing.JLabel jlAppName;
    private javax.swing.JLabel jlAppVersion;
    private javax.swing.JLabel jlBuildInfo;
    private javax.swing.JLabel jlCBIOLogLevel;
    private javax.swing.JLabel jlCBIOServiceState;
    private javax.swing.JLabel jlCBIOServiceState1;
    private javax.swing.JLabel jlCBIOServiceState2;
    private javax.swing.JLabel jlCBIOServiceState3;
    private javax.swing.JLabel jlCoPass;
    private javax.swing.JLabel jlCoPass1;
    private javax.swing.JLabel jlCoPass2;
    private javax.swing.JLabel jlCoPass3;
    private javax.swing.JLabel jlCoURL;
    private javax.swing.JLabel jlCoURL1;
    private javax.swing.JLabel jlCoURL2;
    private javax.swing.JLabel jlCoURL3;
    private javax.swing.JLabel jlCoUser;
    private javax.swing.JLabel jlCoUser1;
    private javax.swing.JLabel jlCoUser2;
    private javax.swing.JLabel jlCoUser3;
    private javax.swing.JLabel jlGoAdmin;
    private javax.swing.JLabel jlPoolDeadtime;
    private javax.swing.JPanel jpCoERP;
    private javax.swing.JPanel jpCoERP1;
    private javax.swing.JPanel jpCoERP2;
    private javax.swing.JPanel jpCoERP3;
    private javax.swing.JPanel jpSettings;
    private javax.swing.JTextField jtAdminName;
    private javax.swing.JPasswordField jtAdminPassword;
    private javax.swing.JPasswordField jtAdminPassword2;
    private javax.swing.JTextField jtCBIOServiceState;
    private javax.swing.JTextField jtCBIOServiceState1;
    private javax.swing.JTextField jtCBIOServiceState2;
    private javax.swing.JTextField jtCBIOServiceState3;
    private javax.swing.JPasswordField jtCoPass;
    private javax.swing.JPasswordField jtCoPass1;
    private javax.swing.JPasswordField jtCoPass2;
    private javax.swing.JPasswordField jtCoPass3;
    private javax.swing.JTextField jtCoURL;
    private javax.swing.JTextField jtCoURL1;
    private javax.swing.JTextField jtCoURL2;
    private javax.swing.JTextField jtCoURL3;
    private javax.swing.JTextField jtCoUser;
    private javax.swing.JTextField jtCoUser1;
    private javax.swing.JTextField jtCoUser2;
    private javax.swing.JTextField jtCoUser3;
    private javax.swing.JTextField jtPoolDeadtime;
    private javax.swing.JTextField jtServerAddr;
    private javax.swing.JTextField jtServerPort;
    private javax.swing.JTextField jtServerPortSSL;
    private javax.swing.JTextArea jtaLicense;
    private javax.swing.JTextArea jtaLog;
    private javax.swing.JTextArea jtaSysOut;
    private javax.swing.JTabbedPane jtbCoERP;
    private javax.swing.JTabbedPane jtbMain;
    // End of variables declaration//GEN-END:variables
}
