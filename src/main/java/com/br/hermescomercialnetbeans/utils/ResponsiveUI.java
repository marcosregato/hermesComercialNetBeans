package com.br.hermescomercialnetbeans.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * Classe utilitária para implementar design responsivo em aplicações Swing
 * Fornece métodos para ajustar componentes dinamicamente baseado no tamanho da tela
 * @author marcos
 */
public class ResponsiveUI {
    
    private static final Logger logger = LogManager.getLogger(ResponsiveUI.class);
    
    // Breakpoints para diferentes tamanhos de tela
    public static final int BREAKPOINT_MOBILE = 768;
    public static final int BREAKPOINT_TABLET = 1024;
    public static final int BREAKPOINT_DESKTOP = 1200;
    public static final int BREAKPOINT_LARGE = 1600;
    
    // Fatores de escala para fontes - ajustados para melhor legibilidade
    private static final float SCALE_MOBILE = 1.0f;
    private static final float SCALE_TABLET = 1.1f;
    private static final float SCALE_DESKTOP = 1.2f;
    private static final float SCALE_LARGE = 1.3f;
    
    /**
     * Aplica responsividade a um JFrame
     * @param frame JFrame principal
     */
    public static void makeResponsive(JFrame frame) {
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                adjustComponents(frame);
            }
        });
        
        // Aplica ajuste inicial
        SwingUtilities.invokeLater(() -> adjustComponents(frame));
        
        logger.info("Responsividade aplicada ao frame: " + frame.getTitle());
    }
    
    /**
     * Aplica responsividade a um JDialog
     * @param dialog JDialog principal
     */
    public static void makeResponsive(JDialog dialog) {
        dialog.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                adjustComponents(dialog);
            }
        });
        
        // Aplica ajuste inicial
        SwingUtilities.invokeLater(() -> adjustComponents(dialog));
        
        logger.info("Responsividade aplicada ao dialog: " + dialog.getTitle());
    }
    
    /**
     * Aplica responsividade a um JInternalFrame
     * @param internalFrame JInternalFrame
     */
    public static void makeResponsive(JInternalFrame internalFrame) {
        internalFrame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                adjustComponents(internalFrame);
            }
        });
        
        // Aplica ajuste inicial
        SwingUtilities.invokeLater(() -> adjustComponents(internalFrame));
        
        logger.debug("Responsividade aplicada ao internal frame: " + internalFrame.getTitle());
    }
    
    /**
     * Ajusta componentes baseado no tamanho do container
     * @param container Container principal
     */
    private static void adjustComponents(Container container) {
        int width = container.getWidth();
        int height = container.getHeight();
        
        DeviceType deviceType = getDeviceType(width);
        float scaleFactor = getScaleFactor(deviceType);
        
        logger.debug("Ajustando componentes para: " + deviceType + " (" + width + "x" + height + ")");
        
        // Ajusta fontes recursivamente
        adjustFonts(container, scaleFactor);
        
        // Ajusta tamanhos de componentes
        adjustComponentSizes(container, deviceType);
        
        // Ajusta layouts
        adjustLayouts(container, deviceType);
    }
    
    /**
     * Determina o tipo de dispositivo baseado na largura
     * @param width largura da tela
     * @return tipo de dispositivo
     */
    public static DeviceType getDeviceType(int width) {
        if (width < BREAKPOINT_MOBILE) {
            return DeviceType.MOBILE;
        } else if (width < BREAKPOINT_TABLET) {
            return DeviceType.TABLET;
        } else if (width < BREAKPOINT_DESKTOP) {
            return DeviceType.DESKTOP;
        } else {
            return DeviceType.LARGE;
        }
    }
    
    /**
     * Obtém o fator de escala para o tipo de dispositivo
     * @param deviceType tipo de dispositivo
     * @return fator de escala
     */
    private static float getScaleFactor(DeviceType deviceType) {
        switch (deviceType) {
            case MOBILE: return SCALE_MOBILE;
            case TABLET: return SCALE_TABLET;
            case DESKTOP: return SCALE_DESKTOP;
            case LARGE: return SCALE_LARGE;
            default: return 1.0f;
        }
    }
    
    /**
     * Ajusta fontes recursivamente
     * @param container container principal
     * @param scaleFactor fator de escala
     */
    private static void adjustFonts(Container container, float scaleFactor) {
        for (Component component : container.getComponents()) {
            if (component instanceof Container) {
                adjustFonts((Container) component, scaleFactor);
            }
            
            if (component instanceof JLabel || component instanceof JButton || 
                component instanceof JTextField || component instanceof JPasswordField) {
                Font originalFont = component.getFont();
                if (originalFont != null) {
                    float newSize = originalFont.getSize() * scaleFactor;
                    Font scaledFont = originalFont.deriveFont(newSize);
                    component.setFont(scaledFont);
                }
            }
        }
    }
    
    /**
     * Ajusta tamanhos de componentes
     * @param container container principal
     * @param deviceType tipo de dispositivo
     */
    private static void adjustComponentSizes(Container container, DeviceType deviceType) {
        for (Component component : container.getComponents()) {
            if (component instanceof Container) {
                adjustComponentSizes((Container) component, deviceType);
            }
            
            // Ajusta preferências de tamanho baseado no dispositivo
            if (component instanceof JButton) {
                adjustButtonSize((JButton) component, deviceType);
            } else if (component instanceof JTextField || component instanceof JPasswordField) {
                adjustTextFieldSize((JTextComponent) component, deviceType);
            }
        }
    }
    
    /**
     * Ajusta tamanho de botões
     * @param button botão a ser ajustado
     * @param deviceType tipo de dispositivo
     */
    private static void adjustButtonSize(JButton button, DeviceType deviceType) {
        int padding = getPaddingForDevice(deviceType);
        
        // Ajusta padding baseado no dispositivo
        Insets newInsets = new Insets(
            padding, padding, padding, padding
        );
        button.setMargin(newInsets);
        
        // Ajusta tamanho mínimo
        int minWidth = getMinButtonWidth(deviceType);
        int minHeight = getMinButtonHeight(deviceType);
        button.setMinimumSize(new Dimension(minWidth, minHeight));
    }
    
    /**
     * Ajusta tamanho de campos de texto
     * @param textField campo de texto a ser ajustado
     * @param deviceType tipo de dispositivo
     */
    private static void adjustTextFieldSize(JTextComponent textField, DeviceType deviceType) {
        int height = getTextFieldHeight(deviceType);
        textField.setPreferredSize(new Dimension(textField.getPreferredSize().width, height));
        textField.setMinimumSize(new Dimension(100, height));
    }
    
    /**
     * Ajusta layouts baseado no dispositivo
     * @param container container principal
     * @param deviceType tipo de dispositivo
     */
    private static void adjustLayouts(Container container, DeviceType deviceType) {
        LayoutManager layout = container.getLayout();
        
        if (layout instanceof BorderLayout) {
            adjustBorderLayout(container, (BorderLayout) layout, deviceType);
        } else if (layout instanceof FlowLayout) {
            adjustFlowLayout(container, (FlowLayout) layout, deviceType);
        } else if (layout instanceof GridBagLayout) {
            adjustGridBagLayout(container, (GridBagLayout) layout, deviceType);
        }
    }
    
    /**
     * Ajusta BorderLayout
     */
    private static void adjustBorderLayout(Container container, BorderLayout layout, DeviceType deviceType) {
        // Em dispositivos móveis, pode reorganizar componentes
        if (deviceType == DeviceType.MOBILE) {
            // Implementar lógica específica para mobile
        }
    }
    
    /**
     * Ajusta FlowLayout
     */
    private static void adjustFlowLayout(Container container, FlowLayout layout, DeviceType deviceType) {
        if (deviceType == DeviceType.MOBILE) {
            layout.setAlignment(FlowLayout.CENTER);
            layout.setHgap(5);
            layout.setVgap(5);
        } else {
            layout.setHgap(10);
            layout.setVgap(10);
        }
    }
    
    /**
     * Ajusta GridBagLayout
     */
    private static void adjustGridBagLayout(Container container, GridBagLayout layout, DeviceType deviceType) {
        // Ajusta insets baseado no dispositivo
        int padding = getPaddingForDevice(deviceType);
        
        for (Component component : container.getComponents()) {
            GridBagConstraints constraints = layout.getConstraints(component);
            if (constraints != null) {
                constraints.insets = new Insets(padding, padding, padding, padding);
                layout.setConstraints(component, constraints);
            }
        }
    }
    
    /**
     * Obtém padding baseado no dispositivo
     */
    private static int getPaddingForDevice(DeviceType deviceType) {
        switch (deviceType) {
            case MOBILE: return 3;
            case TABLET: return 5;
            case DESKTOP: return 8;
            case LARGE: return 10;
            default: return 5;
        }
    }
    
    /**
     * Obtém largura mínima para botões
     */
    private static int getMinButtonWidth(DeviceType deviceType) {
        switch (deviceType) {
            case MOBILE: return 80;
            case TABLET: return 100;
            case DESKTOP: return 120;
            case LARGE: return 140;
            default: return 100;
        }
    }
    
    /**
     * Obtém altura mínima para botões
     */
    private static int getMinButtonHeight(DeviceType deviceType) {
        switch (deviceType) {
            case MOBILE: return 30;
            case TABLET: return 35;
            case DESKTOP: return 40;
            case LARGE: return 45;
            default: return 35;
        }
    }
    
    /**
     * Obtém altura para campos de texto
     */
    private static int getTextFieldHeight(DeviceType deviceType) {
        switch (deviceType) {
            case MOBILE: return 25;
            case TABLET: return 30;
            case DESKTOP: return 35;
            case LARGE: return 40;
            default: return 30;
        }
    }
    
    /**
     * Tipos de dispositivos suportados
     */
    public enum DeviceType {
        MOBILE, TABLET, DESKTOP, LARGE
    }
    
    /**
     * Cria um painel responsivo
     * @param layout tipo de layout
     * @return painel responsivo
     */
    public static JPanel createResponsivePanel(LayoutManager layout) {
        JPanel panel = new JPanel(layout);
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                adjustComponents(panel);
            }
        });
        return panel;
    }
    
    /**
     * Aplica tema responsivo a um componente
     * @param component componente a ser estilizado
     * @param deviceType tipo de dispositivo
     */
    public static void applyResponsiveTheme(Component component, DeviceType deviceType) {
        // Aplica cores e estilos baseados no dispositivo
        if (deviceType == DeviceType.MOBILE) {
            // Estilo para mobile
            if (component instanceof JButton) {
                JButton button = (JButton) component;
                button.setFont(button.getFont().deriveFont(Font.BOLD, 12f));
            }
        }
    }
}
